package com.example.wisdomleafassignment.listItems.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wisdomleafassignment.adapter.ListItemAdapter
import com.example.wisdomleafassignment.base.BaseActivity
import com.example.wisdomleafassignment.base.Status
import com.example.wisdomleafassignment.base.isUserInteractionEnabled
import com.example.wisdomleafassignment.databinding.ActivityTechnologiesBinding
import com.example.wisdomleafassignment.dialog.DescriptionDialog
import com.example.wisdomleafassignment.extra.WebResponse
import com.example.wisdomleafassignment.helper.observe
import com.example.wisdomleafassignment.listItems.model.ListResponse
import com.example.wisdomleafassignment.listItems.viewModel.ListViewModel

class TechnologiesActivity : BaseActivity() {
    private lateinit var binding: ActivityTechnologiesBinding
    private var mAdapter = ListItemAdapter()
    private val listViewModel: ListViewModel by lazy { ListViewModel() }
    private var pageNo = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initViewBinding())
        initRecyclerView()
        observeViewModel()
        listViewModel.getListItems(pageNo)

        binding.swipe.setOnRefreshListener {
            pageNo = 1
            listViewModel.getListItems(pageNo)

        }
    }

    companion object {
        fun startActivity(activity: Activity?) {
            val intent = Intent(activity, TechnologiesActivity::class.java)
            activity?.startActivity(intent)
            activity?.finish()
        }

    }

    private fun observeViewModel() {
        observe(listViewModel.listLiveData, ::handleListResponse)
    }

    //User API response handle method
    private fun handleListResponse(status: WebResponse<ListResponse>) {
        when (status.status) {
            Status.LOADING -> {
                getRootView().isUserInteractionEnabled(false)
            }
            Status.SUCCESS -> status.data?.let {
                binding.swipe.isRefreshing = false
                getRootView().isUserInteractionEnabled(true)
                mAdapter.addAll(it)
            }
            Status.FAILURE -> {
                binding.swipe.isRefreshing = false
                getRootView().isUserInteractionEnabled(true)
                status.errorMsg?.let { showSnackBar(it) }
            }
        }
    }


    private fun initViewBinding(): View {
        binding = ActivityTechnologiesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getRootView(): View {
        return binding.parent
    }

    // initialize list
    private fun initRecyclerView() {
        val linearLayoutManager1 = LinearLayoutManager(this)
        binding.rvTechnology.layoutManager = linearLayoutManager1
        binding.rvTechnology.setHasFixedSize(true)
        binding.rvTechnology.adapter = mAdapter
        mAdapter.setOnItemClickListener(object : ListItemAdapter.OnItemClickListener {
            override fun onItemClick(item: Int) {
                DescriptionDialog.newInstance(mAdapter.items[item].url)
                    .show(supportFragmentManager, "")
            }
        })

        binding.rvTechnology.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val numbers = linearLayoutManager.findLastVisibleItemPosition()

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (numbers == mAdapter.items.size - 1) {
                        pageNo = +pageNo
                        listViewModel.getListItems(pageNo)
                    }
                }
            }
        })
    }
}