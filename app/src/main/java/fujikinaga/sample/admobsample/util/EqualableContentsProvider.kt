package fujikinaga.sample.admobsample.util

import java.util.*

interface EqualableContentsProvider {
    fun providerEqualableContents(): Array<*>

    override fun equals(other: Any?): Boolean // equals()とhashCode()の実装を強制する

    override fun hashCode(): Int

    fun isSameContents(other: Any?): Boolean {
        other ?: return false
        if (other !is EqualableContentsProvider) return false
        if (other::class != this::class) return false
        return other.providerEqualableContents().contentDeepEquals(this.providerEqualableContents())
    }

    fun contentsHash(): Int {
        return Arrays.deepHashCode(arrayOf(this::class, this.providerEqualableContents()))
    }
}