package com.milo.store.presentation.contact

import com.android.milo_store.base.BaseFragment
import com.milo.store.R
import com.milo.store.databinding.FragmentContactBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


private const val TAG = "ContactFragment"

class ContactFragment :
    BaseFragment<FragmentContactBinding, ContactNavigation>() {

    override fun getLayoutId() = R.layout.fragment_contact

    override val viewModel by viewModel<ContactViewModel>()
    override val navigation: ContactNavigation
        get() = ContactNavigation(this)

    override fun initData() {

    }

    override fun observeData() {

    }

    override fun setView() {

    }

    override fun setOnClick() {

    }


}