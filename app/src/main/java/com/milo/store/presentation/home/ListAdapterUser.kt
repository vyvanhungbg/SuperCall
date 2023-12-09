package com.milo.store.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.milo.store.R
import com.milo.store.data.model.User
import com.milo.store.databinding.ItemUserBinding
import com.android.milo_store.base.BaseAdapter
import com.android.milo_store.base.BaseViewHolder


/**
- Create by :Vy Hùng
- Create at :05,November,2023
 **/

class ListAdapterUser(
    private val onClick: (User) -> Unit,
) :
    BaseAdapter<User, BaseViewHolder<User>>(User.diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<User> {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemUserBinding>(inflater, R.layout.item_user, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: ItemUserBinding) :
        BaseViewHolder<User>(binding) {
        override fun binView(item: User) {
            super.binView(item)
            binding.apply {
                user = item
                itemClick = View.OnClickListener {
                    onClick.invoke(item)
                }
                textViewUrl.isSelected = true
            }
        }
    }
}