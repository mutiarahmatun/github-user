package com.dicoding.mutiarahmatun.githubuser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mutiarahmatun.githubuser.databinding.FragmentFollowersBinding
import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowersFragment : Fragment(R.layout.fragment_followers) {

    private var _followersBinding: FragmentFollowersBinding? = null
    private val followersBinding: FragmentFollowersBinding
        get() = requireNotNull(_followersBinding)

    private var users = mutableListOf<Users>()
    private var usersAdapter = UsersAdapter(users)
    var tempFollower = Users("This user has 0 of Follower","", "", "","","","","")
    var tempFollowing = Users("This user has 0 of Following","", "", "","","","","")

    var tab = ""

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_USERNAME = "username"

        @JvmStatic
        fun newInstance(index: Int, username: String) =
            FollowersFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                    putString(ARG_USERNAME, username)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _followersBinding = FragmentFollowersBinding.inflate(inflater, container, false)
        return followersBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followersBinding.rvFollowers.setHasFixedSize(true)
        followersBinding.rvFollowers.adapter = usersAdapter
        followersBinding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        followersBinding.progressBarFollowers.visibility = View.VISIBLE

        getListFollowersOrFollowings()
    }

    private fun getListFollowersOrFollowings() {

        followersBinding.progressBarFollowers.visibility = View.VISIBLE

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(ARG_USERNAME)

        val client = AsyncHttpClient()
        if(index == 1) tab = "following"
        else if(index == 2) tab = "followers"

        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")

        val url = "https://api.github.com/users/${username}/${tab}"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {

                followersBinding.progressBarFollowers.visibility = View.INVISIBLE

                val result = String(responseBody)
                getUsers(result)
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {

                followersBinding.progressBarFollowers.visibility = View.INVISIBLE

                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getUsers(response: String) {

        val listUser = ArrayList<Users>()
        var dummyUser = tempFollower

        if(tab == "following") dummyUser = tempFollowing

        try{
            val dataArray = JSONArray(response)

            for(i in 0 until dataArray.length()){
                val dataObject = dataArray.getJSONObject(i)
                val data = Gson().fromJson(dataObject.toString(), Users::class.java)
                listUser.add(data)
            }

            when {
                listUser.size == 0 -> {
                    users.clear()
                    users.add(dummyUser)
                }

                users.size == 0 -> users.addAll(listUser)

                else -> {
                    users.clear()
                    users.addAll(listUser)
                }
            }

            showRecyclerList(users)

        } catch (e: Exception) {
            makeText(activity, e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun showRecyclerList(user: MutableList<Users>) {
        usersAdapter = UsersAdapter(user)
        followersBinding.rvFollowers.adapter?.notifyDataSetChanged();
    }


}