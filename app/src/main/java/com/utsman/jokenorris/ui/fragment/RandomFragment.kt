package com.utsman.jokenorris.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.utsman.jokenorris.databinding.BottomSheetCategoryBinding
import com.utsman.jokenorris.databinding.FragmentRandomBinding
import com.utsman.jokenorris.domain.ResultState
import com.utsman.jokenorris.domain.entity.Joke
import com.utsman.jokenorris.ui.viewModel.JokeViewModel
import com.utsman.jokenorris.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomFragment : Fragment(layoutRes.fragment_random) {

    private val viewModel: JokeViewModel by activityViewModels()
    private val binding: FragmentRandomBinding by viewBinding()

    private val bottomSheetBinding by lazy {
        val view = LayoutInflater.from(context).inflate(layoutRes.bottom_sheet_category, null)
        BottomSheetCategoryBinding.bind(view)
    }
    private val bottomSheetDialog by lazy {
        BottomSheetDialog(requireContext()).apply {
            setContentView(bottomSheetBinding.root)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onView()
        onBottomView()
    }

    private fun onView() = binding.run {
        viewModel.random.observe(viewLifecycleOwner) { result ->
            networkUI(result)
            tvJoke.isVisible = result is ResultState.Success
            result.onSuccess { joke ->
                tvJoke.text = joke.joke
            }

            btnShuffle.setOnClickListener {
                viewModel.getRandom()
            }
        }
    }

    private fun onBottomView() = bottomSheetBinding.run {
        viewModel.categories.observe(viewLifecycleOwner) { result ->
            binding.btnCategory.isEnabled = result is ResultState.Success

            result.onSuccess { categories ->
                chipCategory.removeAllViews()
                categories.forEach { category ->
                    Chip(context).run {
                        text = category
                        setTextAppearanceResource(styleRes.ThemedTextAppearance)
                        chipCategory.addView(this)

                        setOnClickListener {
                            bottomSheetDialog.dismiss()
                            viewModel.getRandom(category)
                        }
                    }
                }
            }
        }
    }

    private fun FragmentRandomBinding.networkUI(result: ResultState<Joke>) = layoutNetwork.run {
        val isLoading = result is ResultState.Loading
        val isError = result is ResultState.Error
        btnTryAgain.setOnClickListener {
            viewModel.getList()
        }

        btnCategory.setOnClickListener {
            bottomSheetDialog.show()
        }

        prNetwork.isVisible = isLoading
        btnTryAgain.isVisible = isError
        tvError.isVisible = isError

        result.onFailure { th ->
            tvError.text = th.message
        }
    }
}