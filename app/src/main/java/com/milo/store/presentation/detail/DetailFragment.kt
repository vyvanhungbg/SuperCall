package com.milo.store.presentation.detail

import android.content.Intent
import androidx.navigation.fragment.navArgs
import com.milo.store.R
import com.milo.store.databinding.FragmentDetailBinding
import com.android.milo_store.base.BaseFragment
import com.android.milo_store.base.config.ToolbarConfig
import com.android.milo_store.base.extension.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
- Create by :Vy Hùng
- Create at :05,November,2023
 **/
private const val TAG = "DetailFragment"

class DetailFragment :
    BaseFragment<FragmentDetailBinding, DetailNavigation>() {

    override fun getLayoutId() = R.layout.fragment_detail

    override val viewModel by viewModel<DetailViewModel>()
    override val navigation: DetailNavigation
        get() = DetailNavigation(this)

//    private val safeArgs by navArgs<DetailFragmentArgs>()

    override fun initData() {
//        viewModel.setData(safeArgs)
    }

    override fun observeData() {
        viewModel.user.observe(viewLifecycleOwner) {
            if (it == null) {
                // exit if user  invalid
                context?.showToast(getString(R.string.no_user_defined))
                navigation.popBackStack()
            }
        }
    }

    override fun setView() {
        binding.viewModel = viewModel
        binding.layoutHeader.toolbarConfig =
            ToolbarConfig(title = getString(R.string.detail), onBackClick = {
                navigation.popBackStack()
            })
    }

    override fun setOnClick() {
        binding.setOnOpenBrowserClick {
            startActivityIfExists(Intent.ACTION_VIEW, viewModel.user.value?.accountUrl) {
                context?.showToast(getString(R.string.can_not_open_browser))
            }
        }
    }


}