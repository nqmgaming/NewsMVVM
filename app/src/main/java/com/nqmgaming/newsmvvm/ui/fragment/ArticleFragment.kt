package com.nqmgaming.newsmvvm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.nqmgaming.newsmvvm.R
import com.nqmgaming.newsmvvm.databinding.FragmentArticleBinding
import com.nqmgaming.newsmvvm.ui.MainActivity
import com.nqmgaming.newsmvvm.ui.NewsViewModel

class ArticleFragment : Fragment() {
    private lateinit var _binding: FragmentArticleBinding
    private val binding get() = _binding
    val args: ArticleFragmentArgs by navArgs()
    lateinit var viewModel: NewsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)

        viewModel = (requireActivity() as MainActivity).viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = args.article
        binding.apply {
            webView.apply {
                webViewClient = WebViewClient()
                article.url?.let { url ->
                    loadUrl(url)
                }
                // turn on JavaScript
                settings.javaScriptEnabled = true
            }
        }
        binding.fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }
    }
}