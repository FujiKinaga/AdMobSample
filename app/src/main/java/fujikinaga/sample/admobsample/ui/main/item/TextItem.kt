package fujikinaga.sample.admobsample.ui.main.item

import com.xwray.groupie.databinding.BindableItem
import fujikinaga.sample.admobsample.R
import fujikinaga.sample.admobsample.databinding.ItemTextBinding
import fujikinaga.sample.admobsample.ui.main.OnFeedActionListener
import fujikinaga.sample.admobsample.util.EqualableContentsProvider

class TextItem(
    private val text: String,
    private val listener: OnFeedActionListener
    ) : BindableItem<ItemTextBinding>(
    text.hashCode().toLong()
), EqualableContentsProvider {

    override fun bind(binding: ItemTextBinding, position: Int) {
        binding.label.text = text
        binding.root.setOnClickListener {
            listener.onCellClick()
        }
    }

    override fun providerEqualableContents(): Array<*> =
        arrayOf(text.hashCode().toLong())

    override fun equals(other: Any?): Boolean = isSameContents(other)

    override fun hashCode(): Int = contentsHash()

    override fun getLayout(): Int = R.layout.item_text
}