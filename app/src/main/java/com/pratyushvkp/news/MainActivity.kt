package com.pratyushvkp.news

import android.app.DownloadManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      var recyclerView :RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
         fetchData()
        mAdapter = NewsListAdapter(this)
        recyclerView.adapter = mAdapter
    }

    private fun fetchData(){
       val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=52cee89be478439dafd53366404d6d7e"
        val jsonObjectRequest = object: JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                val newsJSONArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for(i in 0 until newsJSONArray.length()){
                    val newsJSONObject = newsJSONArray.getJSONObject(i)
                    val news = News(
                        newsJSONObject.getString("title"),
                        newsJSONObject.getString("author"),
                        newsJSONObject.getString("url"),
                        newsJSONObject.getString("urlToImage")

                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)
            },
            Response.ErrorListener {

            }

        )
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
       val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this,Uri.parse(item.url))

    }


}