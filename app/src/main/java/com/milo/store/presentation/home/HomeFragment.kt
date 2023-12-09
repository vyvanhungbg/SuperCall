package com.milo.store.presentation.home

import android.view.inputmethod.EditorInfo
import com.milo.store.R
import com.milo.store.databinding.FragmentHomeBinding
import com.android.milo_store.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
- Create by :Vy Hùng
- Create at :05,November,2023
 **/
private const val TAG = "HomeFragment"

class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeNavigation>() {

    override fun getLayoutId() = R.layout.fragment_home

    override val viewModel by viewModel<HomeViewModel>()
    override val navigation: HomeNavigation
        get() = HomeNavigation(this)

    private val listAdapterUser by lazy {
        ListAdapterUser(navigation::navigateToDetail)
    }

    override fun initData() {
        context?.let {
            viewModel.initSearchDefault(it)
        }
    }

    override fun observeData() {
        viewModel.users.observe(viewLifecycleOwner) {
            listAdapterUser.submitList(it)
        }
    }

    override fun setView() {
        binding.viewModel = viewModel
        binding.recyclerViewUser.adapter = listAdapterUser
    }

    override fun setOnClick() {
        binding.setOnSearchClick {
            viewModel.searchUsersByName(requireContext())
        }
        binding.textInputEditTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchUsersByName(requireContext())
            }
            true
        }
    }

}