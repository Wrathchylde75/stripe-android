package com.stripe.android.ui.core.elements

import androidx.annotation.RestrictTo
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This is the specification for a country field.
 * @property onlyShowCountryCodes: a list of country code that should be shown.  If empty all
 * countries will be shown.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@Serializable
@Parcelize
data class CountrySpec(
    override val apiPath: IdentifierSpec = IdentifierSpec.Country,

    @SerialName("allowed_countries")
    val onlyShowCountryCodes: Set<String> = supportedBillingCountries
) : FormItemSpec(), RequiredItemSpec {
    fun transform(
        initialValues: Map<IdentifierSpec, String?>
    ) = createSectionElement(
        CountryElement(
            this.apiPath,
            DropdownFieldController(
                CountryConfig(this.onlyShowCountryCodes),
                initialValue = initialValues[this.apiPath]
            )
        )
    )
}
