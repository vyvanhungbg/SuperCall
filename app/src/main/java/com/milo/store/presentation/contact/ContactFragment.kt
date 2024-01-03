package com.milo.store.presentation.contact

import com.android.milo_store.base.BaseFragment
import com.milo.store.ItemContactBindingModel_
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
        viewModel.listContact.observe(viewLifecycleOwner){
            binding.epoxyContact.requestModelBuild()
        }
    }

    override fun setView() {
        context?.let { viewModel.getAllContact(it) }
        initEpoxy()
    }

    override fun setOnClick() {

    }


    private fun initEpoxy() {
        binding.epoxyContact.setHasFixedSize(true)
        binding.epoxyContact.withModels {
            viewModel.listContact.value?.forEach {
                add(
                    ItemContactBindingModel_().id(it.contactId).avatar(it.photoUri).name(it.firstName)
                        .phoneNumber(it.getPhoneNumbersDisplay())
                )
            }
        }
    }
}