package com.stripe.android.ui.core.elements

import androidx.annotation.RestrictTo
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
@Serializable
@Parcelize
data class DropdownSpec(
    @SerialName("api_path")
    override val apiPath: IdentifierSpec,

    @SerialName("label_translation_id")
    val labelTranslationId: TranslationId,

    @SerialName("items")
    val items: List<DropdownItemSpec>
) : FormItemSpec(), RequiredItemSpec {
    fun transform(
        initialValues: Map<IdentifierSpec, String?> = mapOf()
    ) = createSectionElement(
        SimpleDropdownElement(
            this.apiPath,
            DropdownFieldController(
                SimpleDropdownConfig(
                    labelTranslationId.resourceId,
                    items
                ),
                initialValue = initialValues[apiPath]
            )
        )
    )
}
