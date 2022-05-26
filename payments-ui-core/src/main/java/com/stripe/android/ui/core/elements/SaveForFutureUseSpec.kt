package com.stripe.android.ui.core.elements

import androidx.annotation.RestrictTo
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * This is an element that will make elements (as specified by identifier) hidden
 * when save for future use is unchecked
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@Serializable
@Parcelize
data class SaveForFutureUseSpec(
    override val apiPath: IdentifierSpec = IdentifierSpec.SaveForFutureUse
) : FormItemSpec(), RequiredItemSpec {
    fun transform(initialValue: Boolean, merchantName: String): FormElement =
        SaveForFutureUseElement(
            this.apiPath,
            SaveForFutureUseController(
                initialValue
            ),
            merchantName
        )
}
