package com.example.mireapractice.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mireapractice.ui.components.banner.BannerUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    init {
        loadAllNews()
    }

    private fun loadAllNews() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                news = getAllNews(),
                isLoading = false
            )
        }
    }

    private fun getAllNews(): List<BannerUi> {
        return listOf(
            BannerUi(
                title = "Почти 60% банкиров выступили против \"единой цены\" на маркетплейсах",
                subtitle = "Абсолютное большинство представителей российского банковского сообщества (57%) считают, что цена на товары на цифровых платформах может варьироваться в зависимости от способа оплаты. Только 41% опрошенных выступили против скидок на маркетплейсах по картам их банков. Об этом говорится в результатах опроса Ассоциации банков России, который был проведен на профильной банковской конференции.",
                imageUrl = null
            ),
            BannerUi(
                title = "Сварщики бьют рекорды по зарплатам: максимум — 270 тыс. руб.",
                subtitle = "Самая высокая планка среди рабочих профессий, средняя — 190 тыс. руб., за год рост составил 64%.",
                imageUrl = null
            ),
            BannerUi(
                title = "WB и Ozon заполнят товары от индусов",
                subtitle = "Власти России пригласили индийских производителей продуктов питания, товаров для дома, электроники, одежды и обуви выходить на российские маркетплейсы. Это откроет им прямой доступ к миллионам потребителей, причём не только в РФ: «наши маркетплейсы активно проводят международную экспансию и развиваются в соседних с Россией странах», — сказал глава Минэкономразвития Максим Решетников.",
                imageUrl = null
            ),
            BannerUi(
                title = "Цены на SSD взлетят уже в ближайшее время",
                subtitle = "Компания Transcend объявила о приостановлении поставок накопителей. Производитель не получает чипы NAND от Samsung и SanDisk еще с октября — все из-за того, что гиганты перекинули все ресурсы в сторону дата-центров и AI-кластеров. В Transcend считают, что дефицит и высокие цены сохранятся 3–5 месяцев, а пик кризиса придётся на начало 2026 года.",
                imageUrl = null
            ),
            BannerUi(
                title = "Сбер запустил новый продукт для российских компаний-импортеров из Индии",
                subtitle = "Банк с филиалом в Индии, которому уже 15 лет, будет открывать аккредитивы в рупиях и сам платить поставщикам, предоставляя отсрочку российскому импортёру. Процентная ставка при этом будет ниже, чем по кредиту — это позволит повысить надежность сделок и увеличить рост товарооборота, цель по которому 100 млрд долларов к 2030 году. Экономические отношения России и Индии активно развиваются, чему поспособствует и визит Владимира Путина в страну 4-5 декабря.",
                imageUrl = null
            ),
            BannerUi(
                title = "Яндекс Маркет рубит комиссии и дает максимальный контроль над ценами продавцам",
                subtitle = "Первой в эксперименте примет участие категория «Товары для красоты» — с 8 декабря там комиссия упадёт с 48% до 5-12%. Маркетплейс ограничит и собственные скидки на товары сверх цены продавцов – селлеры сами определяют, какую цену видит покупатель. Это тот редкий случай, когда крупный маркетплейс добровольно снижает комиссии и дает бизнесу реальный контроль над ценообразованием.",
                imageUrl = null
            )
        )
    }
}

data class NewsUiState(
    val news: List<BannerUi> = emptyList(),
    val isLoading: Boolean = true
)

