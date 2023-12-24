package com.milo.store.presentation.numpad


import android.annotation.SuppressLint
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import com.android.milo_store.base.BaseFragment
import com.milo.store.ItemBackspaceNumpadBindingModel_
import com.milo.store.ItemEmptyNumpadBindingModel_
import com.milo.store.ItemNumberCircleNormalBindingModel_
import com.milo.store.ItemStartCallGreenBindingModel_
import com.milo.store.R
import com.milo.store.databinding.FragmentNumPadBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


private const val TAG = "NumPadFragment"

class NumPadFragment :
    BaseFragment<FragmentNumPadBinding, NumPadNavigation>() {

    override fun getLayoutId() = R.layout.fragment_num_pad

    override val viewModel by viewModel<NumPadViewModel>()
    override val navigation: NumPadNavigation
        get() = NumPadNavigation(this)

    override fun initData() {

    }

    override fun observeData() {
        viewModel.numberPhone.observe(viewLifecycleOwner) {
            binding.epoxyNumpad.requestModelBuild()
        }
    }

    override fun setView() {
        binding.viewModel = viewModel
        binding.textViewNumberPhone.isSelected = true
        initEpoxy()
    }

    override fun setOnClick() {

    }

    override fun onStop() {
        viewModel.removeMultipleDelete()
        super.onStop()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initEpoxy() {
        binding.epoxyNumpad.withModels {
            ItemNumPad.values().forEachIndexed { index, itemNumPad ->
                when (itemNumPad) {
                    ItemNumPad.EMPTY -> add(ItemEmptyNumpadBindingModel_().id(index))
                    ItemNumPad.CALL -> add(
                        ItemStartCallGreenBindingModel_().id(index).onClick { v ->
                        })

                    ItemNumPad.DELETE -> add(
                        ItemBackspaceNumpadBindingModel_().id(index)
                            .isVisible(viewModel.numberPhone.value?.isNotEmpty() == true)
                            .onClick { v ->
                                // viewModel.clickDeleteNumberPhone()
                            }.onTouchListener { v, event ->
                                when (event?.action) {
                                    MotionEvent.ACTION_DOWN -> {
                                        v?.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                                        viewModel.multipleClickDelete()
                                    }

                                    MotionEvent.ACTION_UP -> {
                                        viewModel.removeMultipleDelete()
                                    }

                                    else -> {

                                    }

                                }
                                false
                            }
                    )

                    else -> add(
                        ItemNumberCircleNormalBindingModel_().id(index).title(itemNumPad.title)
                            .description(itemNumPad.description).onClick { v ->
                                viewModel.clickButtonNormal(itemNumPad.title)
                            }
                    )
                }
            }
        }
    }
}