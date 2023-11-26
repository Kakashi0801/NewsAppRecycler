package com.example.newsapp

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.NetworkError
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.ServerError
import com.android.volley.toolbox.JsonObjectRequest
import com.example.newsapp.databinding.ActivityMainBinding
import org.json.JSONArray


class MainActivity : AppCompatActivity(), NewsItemClicked {
    private var binding: ActivityMainBinding? = null
    private lateinit var mNewsAdapter:NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)
        binding!!.recyclerView.layoutManager= LinearLayoutManager(this)
        fetchData()
        mNewsAdapter = NewsAdapter(this)
        binding!!.recyclerView.adapter=mNewsAdapter


    }

    private fun fetchData() {
        val url ="https://saurav.tech/NewsAPI/top-headlines/category/health/in.json"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {
                // Handle successful response
                val newsJsonArray: JSONArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJsonArray.length()) {
                    val newsJSONObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        if (newsJSONObject.getString("title")==null) "" else newsJSONObject.getString("title"),
                        if (newsJSONObject.getString("author")==null) "" else newsJSONObject.getString("author"),
                        if (newsJSONObject.getString("url")==null) "" else newsJSONObject.getString("url"),
                        if (newsJSONObject.getString("urlToImage")==null) "" else newsJSONObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                mNewsAdapter?.updateNews(newsArray)
            },
            Response.ErrorListener { error ->
                // Handle error
                if (error is NetworkError) {
                    // Handle network error
                    Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show()
                } else if (error is ServerError) {
                    // Handle server error
                    Toast.makeText(this, "Server error", Toast.LENGTH_LONG).show()
                } else if (error is AuthFailureError) {
                    // Handle authentication error
                    Toast.makeText(this, "Authentication error"+error, Toast.LENGTH_LONG).show()
                } else if (error is ParseError) {
                    // Handle parse error
                    Toast.makeText(this, "Parse error", Toast.LENGTH_LONG).show()
                } else {
                    // Handle other errors
                    Toast.makeText(this, "Unknown error", Toast.LENGTH_LONG).show()
                }
            }
        )

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onitemClicked(items: News) {
        val intent = CustomTabsIntent.Builder()
            .build()
        intent.launchUrl(this@MainActivity, Uri.parse(items.url))
    }

}