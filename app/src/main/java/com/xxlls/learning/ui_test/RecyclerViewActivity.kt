package com.xxlls.learning.ui_test

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.xxlls.learning.AnimationActivity
import com.xxlls.learning.R

/**
 * @Date 3/7/22
 * @Author xuzengzeng
 * @Description 列表的测试
 */
class RecyclerViewActivity : AppCompatActivity() {

    lateinit var mRecycler: RecyclerView
    lateinit var mAdapter: TestAdapter
    val items = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_layout)

        initView()
        loadData()
    }

    private fun loadData() {
        for (i in 0..100) {
            items.add("14567890-----position$i")
        }
        updateAdapter()
    }

    private fun updateAdapter() {
        mAdapter.notifyDataSetChanged()
    }

    private fun initView() {
        mAdapter = TestAdapter(items)
        mRecycler = findViewById(R.id.recycler)
        mRecycler.adapter = mAdapter
        mRecycler.addItemDecoration(SingleItemDecoration())
    }


    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, RecyclerViewActivity::class.java)
            context.startActivity(starter)
        }
    }
}