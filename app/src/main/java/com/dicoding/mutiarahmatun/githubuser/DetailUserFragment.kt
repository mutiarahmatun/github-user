package com.dicoding.mutiarahmatun.githubuser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.mutiarahmatun.githubuser.databinding.FragmentDetailUserBinding

class DetailUserFragment (detailsUser: Users) : Fragment(R.layout.fragment_detail_user) {

    private var _detailBinding: FragmentDetailUserBinding? = null
    private val detailBinding: FragmentDetailUserBinding get() = requireNotNull(_detailBinding)
    private var users: Users = detailsUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _detailBinding = FragmentDetailUserBinding.inflate(inflater, container, false)
        return detailBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _detailBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this)
            .load(users.avatar)
            .apply(RequestOptions().override(550, 550))
            .into(detailBinding.imgAvatarReceived)
        detailBinding.tvUsernameReceived.text = StringBuilder("@${users.username}")
        detailBinding.tvNameReceived.text = users.name?: users.username
        detailBinding.tvLocationReceived.text = users.location?: "unknown location"
        detailBinding.tvCompanyReceived.text = users.company?: "unknown company"
        detailBinding.tvRepoReceived.text = users.repository
        detailBinding.tvFollowersReceived.text = users.followers
        detailBinding.tvFollowingReceived.text = users.following
        detailBinding.repository.text = getString(R.string.tag_repo)
        detailBinding.followers.text = getString(R.string.tag_followers)
        detailBinding.following.text = getString(R.string.tag_following)
    }

}