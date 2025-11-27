package com.example.mireapractice.data.remote.banks

data class DaDataBankResponseDto(
    val suggestions: List<BankSuggestionDto>
)

data class BankSuggestionDto(
    val value: String?,
    val unrestrictedValue: String?,
    val data: BankDataDto?
)

data class BankDataDto(
    val bic: String?,
    val swift: String?,
    val inn: String?,
    val kpp: String?,
    val registrationNumber: String?,
    val correspondentAccount: String?,
    val treasuryAccounts: List<String>?,
    val name: BankNameDto?,
    val paymentCity: String?,
    val opf: BankOpfDto?,
    val cbr: BankCbrDto?,
    val address: BankAddressDto?,
    val state: BankStateDto?,
    val okpo: String?,
    val phone: String?,
    val rkc: String?
)

data class BankNameDto(
    val payment: String?,
    val full: String?,
    val short: String?
)

data class BankOpfDto(
    val type: String?,
    val full: String?,
    val short: String?
)

data class BankCbrDto(
    val name: BankNameDto?,
    val bic: String?,
    val address: BankCbrAddressDto?
)

data class BankCbrAddressDto(
    val value: String?,
    val unrestrictedValue: String?,
    val data: BankAddressDataDto?
)

data class BankAddressDto(
    val value: String?,
    val unrestrictedValue: String?,
    val data: BankAddressDataDto?
)

data class BankAddressDataDto(
    val source: String?,
    val qc: Int?
)

data class BankStateDto(
    val actualityDate: Long?,
    val registrationDate: Long?,
    val liquidationDate: Long?,
    val status: String?
)