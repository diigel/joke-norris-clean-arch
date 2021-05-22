package com.utsman.jokenorris.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.utsman.jokenorris.databinding.ActivityMainBinding
import com.utsman.jokenorris.ui.viewModel.JokeViewModel
import com.utsman.jokenorris.utils.idRes
import com.utsman.jokenorris.utils.logD
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: JokeViewModel by viewModels()
    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.WHITE
        onView()
    }

    private fun onView() = binding.run {
        val navHostFragment =
            supportFragmentManager.findFragmentById(idRes.nav_host_container) as NavHostFragment
        NavigationUI.setupWithNavController(bottomNavigation, navHostFragment.navController)

        viewModel.getList()
        viewModel.getRandom()
        viewModel.getCategories()

        btnSearch.setOnClickListener {
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        }
    }
}