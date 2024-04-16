package com.nqmgaming.newsmvvm.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.nqmgaming.newsmvvm.NewsApplication
import com.nqmgaming.newsmvvm.R
import com.nqmgaming.newsmvvm.databinding.ActivityMainBinding
import com.nqmgaming.newsmvvm.db.ArticleDatabase
import com.nqmgaming.newsmvvm.repository.NewsRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val viewModel: NewsViewModel by lazy {
        val repository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory =
            NewModelProviderFactory(application as NewsApplication, repository)
        ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.newsNavHostFragment))
    }
}