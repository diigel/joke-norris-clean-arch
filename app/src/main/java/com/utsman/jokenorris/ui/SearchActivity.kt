package com.utsman.jokenorris.ui

import android.graphics.Color
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.utsman.jokenorris.databinding.ActivitySearchBinding
import com.utsman.jokenorris.databinding.FragmentListBinding
import com.utsman.jokenorris.domain.ResultState
import com.utsman.jokenorris.domain.entity.Joke
import com.utsman.jokenorris.ui.adapter.JokeAdapter
import com.utsman.jokenorris.ui.viewModel.JokeViewModel
import com.utsman.jokenorris.utils.onFailure
import com.utsman.jokenorris.utils.onSuccess
import com.utsman.jokenorris.utils.watcher
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private val viewModel: JokeViewModel by viewModels()
    private val jokeAdapter = JokeAdapter()
    private val binding: ActivitySearchBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.WHITE
        onView()
    }

    private fun onView() = binding.run {
        rvJoke.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = jokeAdapter
        }

        etSearch.watcher { query ->
            viewModel.search(query)
        }

        viewModel.searchResult.observe(this@SearchActivity) { result ->
            networkUI(result)

            val isSuccess = result is ResultState.Success
            rvJoke.isVisible = isSuccess
            result.onSuccess { jokes ->
                jokeAdapter.updateList(jokes)
            }
        }
    }

    private fun ActivitySearchBinding.networkUI(result: ResultState<List<Joke>>) = layoutNetwork.run {
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