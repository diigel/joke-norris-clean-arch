package com.utsman.jokenorris.ui.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.utsman.jokenorris.databinding.FragmentListBinding
import com.utsman.jokenorris.domain.ResultState
import com.utsman.jokenorris.domain.entity.Joke
import com.utsman.jokenorris.ui.adapter.JokeAdapter
import com.utsman.jokenorris.ui.viewModel.JokeViewModel
import com.utsman.jokenorris.utils.layoutRes
import com.utsman.jokenorris.utils.onFailure
import com.utsman.jokenorris.utils.onSuccess
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment(layoutRes.fragment_list) {

    private val viewModel: JokeViewModel by activityViewModels()
    private val jokeAdapter = JokeAdapter()
    private val binding: FragmentListBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onView()
    }

    private fun onView() = binding.run {
        rvJoke.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = jokeAdapter
        }

        viewModel.list.observe(viewLifecycleOwner) { result ->
            networkUI(result)

            val isSuccess = result is ResultState.Success
            rvJoke.isVisible = isSuccess
            result.onSuccess { jokes ->
                jokeAdapter.updateList(jokes)
            }
        }

        btnShuffle.setOnClickListener {
            viewModel.getList()
        }
    }

    private fun FragmentListBinding.networkUI(result: ResultState<List<Joke>>) = layoutNetwork.run {
        val isLoading = result is ResultState.Loading
        val isError = result is ResultState.Error
        btnTryAgain.setOnClickListener {
            viewModel.getList()
        }

        prNetwork.isVisible = isLoading
        btnTryAgain.isVisible = isError
        tvError.isVisible = isError

        result.onFailure { th ->
            tvError.text = th.message
        }
    }
}