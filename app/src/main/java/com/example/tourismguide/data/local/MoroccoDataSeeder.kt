package com.example.tourismguide.data.local

import com.example.tourismguide.data.local.dao.ActivityDao
import com.example.tourismguide.data.local.dao.ArchitectureDao
import com.example.tourismguide.data.local.dao.ArtisanatDao
import com.example.tourismguide.data.local.dao.CircuitDao
import com.example.tourismguide.data.local.dao.CityDao
import com.example.tourismguide.data.local.dao.ContentIndexDao
import com.example.tourismguide.data.local.dao.ContentRichDetailDao
import com.example.tourismguide.data.local.dao.CultureDao
import com.example.tourismguide.data.local.dao.DishDao
import com.example.tourismguide.data.local.dao.EventDao
import com.example.tourismguide.data.local.dao.FestivalDao
import com.example.tourismguide.data.local.dao.LocalProductDao
import com.example.tourismguide.data.local.dao.MonumentDao
import com.example.tourismguide.data.local.dao.MuseumDao
import com.example.tourismguide.data.local.dao.MusicDao
import com.example.tourismguide.data.local.dao.NatureDao
import com.example.tourismguide.data.local.dao.PlaceDao
import com.example.tourismguide.data.local.dao.RecommendationDao
import com.example.tourismguide.data.local.dao.RegionDao
import com.example.tourismguide.data.local.dao.TourismOfficeDao
import com.example.tourismguide.data.local.dao.UnescoDao
import com.example.tourismguide.data.local.entity.ActivityEntity
import com.example.tourismguide.data.local.entity.ArchitectureEntity
import com.example.tourismguide.data.local.entity.ArtisanatEntity
import com.example.tourismguide.data.local.entity.CircuitEntity
import com.example.tourismguide.data.local.entity.CircuitStepEntity
import com.example.tourismguide.data.local.entity.CityEntity
import com.example.tourismguide.data.local.entity.ContentIndexEntity
import com.example.tourismguide.data.local.entity.ContentRichDetailEntity
import com.example.tourismguide.data.local.entity.CultureEntity
import com.example.tourismguide.data.local.entity.DishEntity
import com.example.tourismguide.data.local.entity.EventEntity
import com.example.tourismguide.data.local.entity.FestivalEntity
import com.example.tourismguide.data.local.entity.LocalProductEntity
import com.example.tourismguide.data.local.entity.MonumentEntity
import com.example.tourismguide.data.local.entity.MuseumEntity
import com.example.tourismguide.data.local.entity.MusicStyleEntity
import com.example.tourismguide.data.local.entity.NatureSiteEntity
import com.example.tourismguide.data.local.entity.PlaceEntity
import com.example.tourismguide.data.local.entity.RecommendationEntity
import com.example.tourismguide.data.local.entity.RegionEntity
import com.example.tourismguide.data.local.entity.TourismOfficeEntity
import com.example.tourismguide.data.local.entity.UnescoHeritageEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoroccoDataSeeder @Inject constructor(
    private val contentIndexDao: ContentIndexDao,
    private val contentRichDetailDao: ContentRichDetailDao,
    private val regionDao: RegionDao,
    private val cityDao: CityDao,
    private val monumentDao: MonumentDao,
    private val cultureDao: CultureDao,
    private val architectureDao: ArchitectureDao,
    private val musicDao: MusicDao,
    private val dishDao: DishDao,
    private val localProductDao: LocalProductDao,
    private val festivalDao: FestivalDao,
    private val artisanatDao: ArtisanatDao,
    private val natureDao: NatureDao,
    private val activityDao: ActivityDao,
    private val museumDao: MuseumDao,
    private val unescoDao: UnescoDao,
    private val eventDao: EventDao,
    private val circuitDao: CircuitDao,
    private val tourismOfficeDao: TourismOfficeDao,
    private val recommendationDao: RecommendationDao,
    private val placeDao: PlaceDao
) {
    suspend fun seedIfEmpty() {
        if (contentIndexDao.count() > 0) return
        seedAll()
    }

    suspend fun seedAll() {
        val regions = regions()
        regionDao.upsertAll(regions)

        val cities = cities()
        cityDao.upsertAll(cities)

        val monuments = monuments()
        monumentDao.upsertAll(monuments)

        cultureDao.upsertAll(cultureItems())
        architectureDao.upsertAll(architectureItems())
        musicDao.upsertAll(musicStyles())
        dishDao.upsertAll(dishes())
        localProductDao.upsertAll(localProducts())
        festivalDao.upsertAll(festivals())
        artisanatDao.upsertAll(artisanatItems())
        natureDao.upsertAll(natureSites())
        activityDao.upsertAll(activities())
        museumDao.upsertAll(museums())
        unescoDao.upsertAll(unescoSites())
        eventDao.upsertAll(events())
        tourismOfficeDao.upsertAll(tourismOffices())

        val circuits = circuits()
        circuitDao.upsertAll(circuits)
        circuitDao.upsertSteps(circuitSteps())

        recommendationDao.upsertAll(recommendations())
        placeDao.upsertAll(placesFromContent(cities, monuments))
        contentIndexDao.upsertAll(buildContentIndex(cities, monuments))
        contentRichDetailDao.upsertAll(richDetails())
    }

    private fun richDetails() = listOf(
        ContentRichDetailEntity(
            contentId = "music-m-gnawa",
            richBody = """
                Gnawa music is one of Morocco's most powerful spiritual traditions. Born from the encounter between sub-Saharan cultures and Sufi brotherhoods, it combines hypnotic rhythms with call-and-response singing during all-night lila ceremonies.

                The guembri—a three-stringed bass lute—anchors every ensemble, while metal qraqeb castanets drive the trance-inducing pulse. In Essaouira and Marrakech, Gnawa has evolved from sacred ritual into a celebrated world music genre without losing its healing roots.

                Today the Gnaoua World Music Festival transforms Essaouira's medina into a global stage where Maalems preserve ancestral repertoire alongside jazz, blues, and electronic fusion.
            """.trimIndent(),
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1510414842594-a61c69b5ae57?w=800",
                "https://images.unsplash.com/photo-1519046904884-53103b34b206?w=800",
                "https://images.unsplash.com/photo-1536728033382-9963497c2a99?w=800"
            ).joinToString("|"),
            leadersJson = """[
                {"name":"Maalem Mokhtar Gania","role":"Maalem","bio":"Legendary guembri master from Essaouira who brought Gnawa to international festivals.","imageUrl":"https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400"},
                {"name":"Maalem Hamid El Kasri","role":"Maalem","bio":"Renowned for his deep voice and mastery of the Gnawi repertoire.","imageUrl":"https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=400"}
            ]"""
        ),
        ContentRichDetailEntity(
            contentId = "music-m-andalusi",
            richBody = """
                Andalusian music arrived in Morocco with Muslim and Jewish refugees fleeing the Reconquista. Preserved in Fès and Tétouan, this classical tradition features ornate suites called nuba performed by orchestras of oud, rebab, and percussion.

                Each nuba follows a strict modal system (tab') unfolding over hours. Courtly poetry in classical Arabic intertwines with instrumental interludes of breathtaking virtuosity.

                Listening in a Fassi foundouk or during the Fès Festival of World Sacred Music reveals Morocco's deepest link to Al-Andalus.
            """.trimIndent(),
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1536728033382-9963497c2a99?w=800",
                "https://images.unsplash.com/photo-1548018560-c7196548e84d?w=800"
            ).joinToString("|"),
            leadersJson = """[
                {"name":"Abdelkrim Rais","role":"Chef d'orchestre","bio":"Led the Orchestre National du Maroc in reviving Andalusian suites.","imageUrl":"https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400"}
            ]"""
        ),
        ContentRichDetailEntity(
            contentId = "monument-hassan-ii",
            richBody = """
                Rising from the Atlantic shoreline, the Hassan II Mosque is a masterpiece of modern Moroccan craftsmanship. Completed in 1993, its 210-meter minaret is the tallest in the world.

                The prayer hall accommodates 25,000 worshippers beneath a retractable roof. Artisans from across the kingdom contributed zellige, carved cedar, and intricate plasterwork.

                Non-Muslim visitors can join guided tours to admire the underground hammam, laser-directed qibla, and the glass floor section revealing the ocean below.
            """.trimIndent(),
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1548018560-c7196548e84d?w=800",
                "https://images.unsplash.com/photo-1519046904884-53103b34b206?w=800"
            ).joinToString("|")
        ),
        ContentRichDetailEntity(
            contentId = "dish-d-couscous",
            richBody = """
                Couscous is Morocco's Friday tradition—a communal dish steamed in a special pot called a couscoussier. Semolina grains are raked and steamed three times until impossibly light.

                The topping varies by region: seven-vegetable couscous in the north, sweet caramelized onions with raisins in Fès, or tfaya with chickpeas and cinnamon in the Atlas.

                Sharing couscous from a single platter embodies Moroccan hospitality at its warmest.
            """.trimIndent(),
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1510414842594-a61c69b5ae57?w=800",
                "https://images.unsplash.com/photo-1489493173502-14ec3a0c5bb2?w=800"
            ).joinToString("|")
        )
    )

    private fun img(seed: String) = "https://images.unsplash.com/photo-$seed?w=800"

    private fun regions() = listOf(
        RegionEntity("r-tanger", "Tanger-Tétouan-Al Hoceïma", "Northern gateway where Mediterranean meets Atlantic.", img("1548018560-c7196548e84d")),
        RegionEntity("r-rabat", "Rabat-Salé-Kénitra", "Capital region with imperial heritage and Atlantic coast.", img("1510414842594-a61c69b5ae57")),
        RegionEntity("r-fes", "Fès-Meknès", "Spiritual and artisan heart of Morocco.", img("1536728033382-9963497c2a99")),
        RegionEntity("r-marrakech", "Marrakech-Safi", "Red city, Atlas foothills and Atlantic surf.", img("1519046904884-53103b34b206")),
        RegionEntity("r-souss", "Souss-Massa", "Agadir, Anti-Atlas and Berber traditions.", img("1489493173502-14ec3a0c5bb2")),
        RegionEntity("r-draa", "Drâa-Tafilalet", "Oases, kasbahs and Sahara gateways.", img("1509316785379-5491c7adf4ca")),
        RegionEntity("r-oriental", "Oriental", "Eastern Morocco, music and desert frontiers.", img("1469856853081-bb74dfbb6cbd")),
        RegionEntity("r-beni", "Béni Mellal-Khénifra", "Middle Atlas lakes, waterfalls and cedar forests.", img("1506905925346-be21c11fd8de"))
    )

    private fun cities() = listOf(
        city("rabat", "Rabat", "Morocco's capital blends imperial history with a modern waterfront.", "r-rabat", 580000, 34.0209, -6.8416, "Mediterranean", "Founded as Chellah, capital since 1912.", img("1548018560-c7196548e84d"), 95),
        city("casablanca", "Casablanca", "Economic capital and home to Hassan II Mosque.", "r-rabat", 3400000, 33.5731, -7.5898, "Oceanic", "Transformed by French colonial planning.", img("1548018560-c7196548e84d"), 98),
        city("marrakech", "Marrakech", "The Red City — souks, palaces and Jemaa el-Fna.", "r-marrakech", 930000, 31.6295, -7.9811, "Semi-arid", "Founded 1070 by Almoravids.", img("1510414842594-a61c69b5ae57"), 100),
        city("fes", "Fès", "World's largest car-free urban area and spiritual capital.", "r-fes", 1150000, 34.0181, -5.0078, "Continental", "Founded 789 by Idris I.", img("1536728033382-9963497c2a99"), 97),
        city("meknes", "Meknès", "Imperial city of Moulay Ismail with grand gates.", "r-fes", 632000, 33.8935, -5.5473, "Continental", "17th-century imperial capital.", img("1536728033382-9963497c2a99"), 88),
        city("tangier", "Tangier", "Cosmopolitan port at the Strait of Gibraltar.", "r-tanger", 947000, 35.7595, -5.8340, "Mediterranean", "Ancient Phoenician settlement.", img("1548018560-c7196548e84d"), 90),
        city("tetouan", "Tétouan", "Andalusian medina and UNESCO heritage.", "r-tanger", 380000, 35.5889, -5.3626, "Mediterranean", "Rebuilt by Andalusian refugees.", img("1536728033382-9963497c2a99"), 82),
        city("chefchaouen", "Chefchaouen", "The Blue Pearl of the Rif Mountains.", "r-tanger", 43000, 35.1714, -5.2697, "Mountain", "Founded 1471 as fortress.", img("1536728033382-9963497c2a99"), 94),
        city("essaouira", "Essaouira", "Windy Atlantic medina of art and music.", "r-marrakech", 78000, 31.5085, -9.7595, "Oceanic", "Portuguese and French influences.", img("1519046904884-53103b34b206"), 91),
        city("agadir", "Agadir", "Modern resort city rebuilt after 1960 earthquake.", "r-souss", 421000, 30.4278, -9.5981, "Oceanic", "Ancient fishing port.", img("1519046904884-53103b34b206"), 85),
        city("ouarzazate", "Ouarzazate", "Gateway to the desert and Hollywood of Morocco.", "r-draa", 71000, 30.9335, -6.9370, "Desert", "French garrison town.", img("1489493173502-14ec3a0c5bb2"), 87),
        city("dakhla", "Dakhla", "Lagoon paradise in the Western Sahara.", "r-souss", 106000, 23.6847, -15.9579, "Desert-ocean", "Historic trade post.", img("1489493173502-14ec3a0c5bb2"), 80),
        city("merzouga", "Merzouga", "Village at the edge of Erg Chebbi dunes.", "r-draa", 3000, 31.0802, -4.0141, "Saharan", "Berber settlement.", img("1489493173502-14ec3a0c5bb2"), 93),
        city("ifrane", "Ifrane", "Alpine-style ski resort in the Middle Atlas.", "r-beni", 14000, 33.5228, -5.1106, "Mountain", "Built by French in 1929.", img("1506905925346-be21c11fd8de"), 78)
    )

    private fun city(id: String, name: String, desc: String, regionId: String, pop: Long, lat: Double, lng: Double, climate: String, history: String, image: String, pop2: Int) =
        CityEntity(id, name, desc, regionId, pop, lat, lng, climate, history, image, 4.6, pop2)

    private fun monuments() = listOf(
        monument("hassan-ii", "Hassan II Mosque", "Largest mosque in Morocco, built on the Atlantic.", "casablanca", 33.6085, -7.6328, "130 MAD", "9h-14h, 15h-18h", "mosque", img("1548018560-c7196548e84d"), 99),
        monument("koutoubia", "Koutoubia Mosque", "Iconic minaret dominating Marrakech skyline.", "marrakech", 31.6236, -7.9936, "Free (exterior)", "Always visible", "mosque", img("1510414842594-a61c69b5ae57"), 96),
        monument("bab-mansour", "Bab Mansour", "Grandest gate of Meknès imperial city.", "meknes", 33.8922, -5.5647, "Free", "8h-18h", "gate", img("1536728033382-9963497c2a99"), 90),
        monument("volubilis", "Volubilis", "Roman archaeological site with stunning mosaics.", "meknes", 34.0742, -5.5553, "70 MAD", "9h-17h", "archaeological", img("1469856853081-bb74dfbb6cbd"), 92),
        monument("ait-ben-haddou", "Ait Ben Haddou", "Fortified ksar and UNESCO kasbah.", "ouarzazate", 31.0470, -7.1319, "Free", "Sunrise-sunset", "kasbah", img("1489493173502-14ec3a0c5bb2"), 95),
        monument("chellah", "Chellah Necropolis", "Ancient Roman and Marinid ruins in Rabat.", "rabat", 34.0069, -6.8361, "70 MAD", "9h-17h", "archaeological", img("1548018560-c7196548e84d"), 85),
        monument("medina-fes", "Fès el-Bali Medina", "Medieval medina and world's oldest university.", "fes", 34.0631, -4.9736, "Free", "Always open", "medina", img("1536728033382-9963497c2a99"), 98),
        monument("kasbah-oudayas", "Kasbah of the Udayas", "Blue-and-white fortress overlooking Bou Regreg.", "rabat", 34.0250, -6.8364, "Free", "9h-17h", "kasbah", img("1548018560-c7196548e84d"), 88)
    )

    private fun monument(id: String, name: String, desc: String, cityId: String, lat: Double, lng: Double, price: String, hours: String, type: String, image: String, pop: Int) =
        MonumentEntity(id, name, desc, "Historic landmark of Morocco.", cityId, lat, lng, price, hours, type, image, 4.7, pop)

    private fun cultureItems() = listOf(
        CultureEntity("c-history", "History of Morocco", "From Amazigh kingdoms to the Alaouite dynasty.", "Berber, Arab, Andalusian and African threads woven over millennia.", "history", img("1536728033382-9963497c2a99"), "r-rabat"),
        CultureEntity("c-amazigh", "Amazigh Culture", "Indigenous heritage of the Atlas and Rif.", "Tamazight language, Ahidous dance and tribal traditions.", "amazigh", img("1506905925346-be21c11fd8de"), "r-beni"),
        CultureEntity("c-andalusian", "Andalusian Influence", "Refugee artisans shaped Tétouan and Fès.", "Music, zellige and architecture from Al-Andalus.", "andalusian", img("1536728033382-9963497c2a99"), "r-fes"),
        CultureEntity("c-spiritual", "Spiritual Heritage", "Sufi brotherhoods and sacred music.", "Moussem festivals and zaouias across the country.", "spiritual", img("1510414842594-a61c69b5ae57"), "r-fes"),
        CultureEntity("c-traditions", "Traditions & Customs", "Hospitality, hammams and mint tea rituals.", "Daily life shaped by Islam and Berber customs.", "traditions", img("1510414842594-a61c69b5ae57"), null)
    )

    private fun architectureItems() = listOf(
        ArchitectureEntity("a-riad", "Riads", "Traditional courtyard houses in medinas.", "Marrakech and Fès riads feature zellige and cedar.", "riad", "r-marrakech", img("1510414842594-a61c69b5ae57"), "koutoubia"),
        ArchitectureEntity("a-zellige", "Zellige", "Hand-cut geometric tilework.", "Each tile is chiseled by master craftsmen.", "zellige", "r-fes", img("1536728033382-9963497c2a99"), "medina-fes"),
        ArchitectureEntity("a-kasbah", "Kasbahs", "Fortified earthen architecture of the south.", "Ait Ben Haddou exemplifies southern kasbah style.", "kasbah", "r-draa", img("1489493173502-14ec3a0c5bb2"), "ait-ben-haddou"),
        ArchitectureEntity("a-minaret", "Minarets", "Prayer towers defining skylines.", "Koutoubia inspired La Giralda in Seville.", "minaret", "r-marrakech", img("1510414842594-a61c69b5ae57"), "koutoubia")
    )

    private fun musicStyles() = listOf(
        MusicStyleEntity("m-gnawa", "Gnawa", "Trance music of sub-Saharan origin.", "Brought by enslaved peoples, now UNESCO heritage.", "guembri, qraqeb", "Essaouira, Marrakech", "Maalem Mokhtar Gania", img("1510414842594-a61c69b5ae57")),
        MusicStyleEntity("m-chaabi", "Chaabi", "Popular urban folk music.", "Everyday soundtrack of Moroccan cities.", "oud, violin", "Casablanca, Rabat", "Nass El Ghiwane", img("1548018560-c7196548e84d")),
        MusicStyleEntity("m-ahidous", "Ahidous", "Amazigh collective dance and song.", "Performed at weddings and festivals.", "bendir", "Middle Atlas", "Local troupes", img("1506905925346-be21c11fd8de")),
        MusicStyleEntity("m-andalusi", "Andalusian Music", "Classical court music from Al-Andalus.", "Preserved in Fès and Tétouan.", "oud, rebab", "Fès, Tétouan", "Orchestre National", img("1536728033382-9963497c2a99")),
        MusicStyleEntity("m-malhoun", "Malhoun", "Poetic sung prose from artisan guilds.", "Literary and musical tradition.", "oud", "Marrakech", "Traditional ensembles", img("1510414842594-a61c69b5ae57"))
    )

    private fun dishes() = listOf(
        dish("d-couscous", "Couscous", "Friday staple of steamed semolina with vegetables.", "Berber origin, national dish.", "semolina, vegetables, chickpeas, meat", "r-fes", img("1510414842594-a61c69b5ae57")),
        dish("d-tajine", "Tajine", "Slow-cooked stew in conical clay pot.", "Named after the cooking vessel.", "meat, prunes, olives, spices", "r-marrakech", img("1510414842594-a61c69b5ae57")),
        dish("d-pastilla", "Pastilla", "Sweet-savory pigeon pie with cinnamon.", "Fassi specialty for celebrations.", "pigeon, almonds, cinnamon, warka", "r-fes", img("1536728033382-9963497c2a99")),
        dish("d-harira", "Harira", "Hearty soup breaking Ramadan fast.", "Tomato, lentils and lamb.", "tomatoes, lentils, lamb, herbs", "r-marrakech", img("1510414842594-a61c69b5ae57")),
        dish("d-tanjia", "Tanjia", "Marrakchi slow-cooked meat in clay jar.", "Cooked in hammam ashes.", "beef, saffron, preserved lemon", "r-marrakech", img("1510414842594-a61c69b5ae57")),
        dish("d-mechoui", "Mechoui", "Whole roasted lamb.", "Feast centerpiece in Atlas villages.", "lamb, cumin, salt", "r-marrakech", img("1489493173502-14ec3a0c5bb2")),
        dish("d-mint-tea", "Mint Tea", "Moroccan hospitality in a glass.", "Gunpowder green tea with fresh mint.", "gunpowder tea, mint, sugar", "r-marrakech", img("1510414842594-a61c69b5ae57"), "drink")
    )

    private fun dish(id: String, name: String, desc: String, history: String, ingredients: String, regionId: String, image: String, type: String = "dish") =
        DishEntity(id, name, desc, history, ingredients, regionId, type, image, 4.8)

    private fun localProducts() = listOf(
        LocalProductEntity("p-argan", "Argan Oil", "Liquid gold from Souss cooperatives.", "Berber women's cooperatives produce cosmetic and culinary oil.", "r-souss", img("1489493173502-14ec3a0c5bb2")),
        LocalProductEntity("p-saffron", "Saffron", "Taliouine red gold.", "Morocco's finest saffron from Anti-Atlas.", "r-souss", img("1510414842594-a61c69b5ae57")),
        LocalProductEntity("p-dates", "Dates", "Sweet harvest of the Draa Valley.", "Over 100 varieties cultivated in oases.", "r-draa", img("1489493173502-14ec3a0c5bb2")),
        LocalProductEntity("p-amlou", "Amlou", "Almond and argan spread.", "Berber breakfast delicacy with honey.", "r-souss", img("1489493173502-14ec3a0c5bb2"))
    )

    private fun festivals() = listOf(
        FestivalEntity("f-mawazine", "Mawazine", "International music festival in Rabat.", "May", "World-class concerts across the capital.", "rabat", 34.0209, -6.8416, img("1548018560-c7196548e84d")),
        FestivalEntity("f-gnaoua", "Gnaoua World Music Festival", "Essaouira celebrates Gnawa heritage.", "June", "Gnawa masters and international fusion.", "essaouira", 31.5085, -9.7595, img("1519046904884-53103b34b206")),
        FestivalEntity("f-timitar", "Timitar Festival", "Amazigh and world music in Agadir.", "July", "Celebrates Souss musical heritage.", "agadir", 30.4278, -9.5981, img("1519046904884-53103b34b206")),
        FestivalEntity("f-rose", "Rose Festival", "Kelaat M'Gouna celebrates the rose harvest.", "May", "Parades, music and rose water.", "ouarzazate", 31.2500, -6.1333, img("1489493173502-14ec3a0c5bb2")),
        FestivalEntity("f-cherry", "Cherry Festival", "Sefrou celebrates spring harvest.", "June", "Traditional processions and folklore.", "fes", 33.8300, -4.8300, img("1536728033382-9963497c2a99"))
    )

    private fun artisanatItems() = listOf(
        ArtisanatEntity("art-carpet", "Carpets", "Hand-woven Berber and Arab rugs.", "carpet", "r-fes", img("1536728033382-9963497c2a99")),
        ArtisanatEntity("art-pottery", "Pottery", "Fassi blue and Safi ceramics.", "pottery", "r-fes", img("1536728033382-9963497c2a99")),
        ArtisanatEntity("art-leather", "Leather", "Tanneries of Fès and Marrakech.", "leather", "r-fes", img("1536728033382-9963497c2a99")),
        ArtisanatEntity("art-copper", "Copper", "Engraved trays and lanterns.", "copper", "r-marrakech", img("1510414842594-a61c69b5ae57")),
        ArtisanatEntity("art-caftan", "Caftans", "Embroidered ceremonial dress.", "embroidery", "r-fes", img("1536728033382-9963497c2a99"))
    )

    private fun natureSites() = listOf(
        NatureSiteEntity("n-atlas", "High Atlas", "Snow-capped peaks and Berber villages.", "mountain", "r-marrakech", 31.0617, -7.9161, img("1506905925346-be21c11fd8de"), 4.9),
        NatureSiteEntity("n-erg-chebbi", "Erg Chebbi", "Golden dunes up to 150m high.", "desert", "r-draa", 31.1636, -3.9869, img("1489493173502-14ec3a0c5bb2"), 4.9),
        NatureSiteEntity("n-legzira", "Legzira Beach", "Natural stone arches on Atlantic coast.", "beach", "r-souss", 29.4044, -10.0872, img("1519046904884-53103b34b206"), 4.7),
        NatureSiteEntity("n-ouzoud", "Ouzoud Waterfalls", "110m cascade in Middle Atlas.", "waterfall", "r-beni", 32.0153, -6.7194, img("1506905925346-be21c11fd8de"), 4.8),
        NatureSiteEntity("n-toubkal", "Toubkal National Park", "Highest peak in North Africa at 4,167m.", "national_park", "r-marrakech", 31.0592, -7.9167, img("1506905925346-be21c11fd8de"), 4.9),
        NatureSiteEntity("n-draa", "Draa Valley", "Palm oases and kasbah route.", "valley", "r-draa", 30.5000, -5.5000, img("1489493173502-14ec3a0c5bb2"), 4.8)
    )

    private fun activities() = listOf(
        ActivityEntity("act-surf", "Surfing", "World-class breaks at Taghazout and Dakhla.", "surf", "r-souss", 30.5450, -9.7080, "200-800 MAD", img("1519046904884-53103b34b206"), 4.8),
        ActivityEntity("act-hike", "Hiking", "Atlas trails from Imlil to Toubkal.", "hiking", "r-marrakech", 31.1342, -7.9183, "Guide required", img("1506905925346-be21c11fd8de"), 4.9),
        ActivityEntity("act-camel", "Camel Trekking", "Sunset rides on Erg Chebbi dunes.", "camel", "r-draa", 31.0802, -4.0141, "300-600 MAD", img("1489493173502-14ec3a0c5bb2"), 4.9),
        ActivityEntity("act-quad", "Quad Biking", "Desert adventure near Merzouga.", "quad", "r-draa", 31.1000, -4.0000, "400 MAD", img("1489493173502-14ec3a0c5bb2"), 4.5),
        ActivityEntity("act-ski", "Skiing", "Michlifen and Ifrane winter sports.", "ski", "r-beni", 33.4000, -5.1000, "Lift pass 150 MAD", img("1506905925346-be21c11fd8de"), 4.3),
        ActivityEntity("act-golf", "Golf", "Palmeraie and coastal courses.", "golf", "r-marrakech", 31.7000, -8.0000, "500+ MAD", img("1510414842594-a61c69b5ae57"), 4.6)
    )

    private fun museums() = listOf(
        MuseumEntity("mu-yves", "Yves Saint Laurent Museum", "Fashion and Moroccan art in Marrakech.", "Dedicated to YSL's love of Morocco.", "marrakech", 31.6425, -8.0031, "100 MAD", "10h-18h", img("1510414842594-a61c69b5ae57"), 4.8),
        MuseumEntity("mu-mohammed6", "Mohammed VI Museum", "Modern and contemporary Moroccan art.", "Rabat's premier art museum.", "rabat", 34.0136, -6.8316, "40 MAD", "10h-18h", img("1548018560-c7196548e84d"), 4.6),
        MuseumEntity("mu-dar-batha", "Dar Batha Museum", "Fassi crafts and ceramics collection.", "Housed in 19th-century palace.", "fes", 34.0636, -4.9731, "20 MAD", "9h-17h", img("1536728033382-9963497c2a99"), 4.5),
        MuseumEntity("mu-berber", "Berber Museum", "Amazigh culture in Majorelle Garden.", "Paul Jacoulet collection.", "marrakech", 31.6417, -8.0033, "Included with garden", "8h-18h", img("1510414842594-a61c69b5ae57"), 4.7)
    )

    private fun unescoSites() = listOf(
        UnescoHeritageEntity("u-fes", "Medina of Fès", "Medieval urban fabric and Al-Qarawiyyin.", 1981, "fes", 34.0631, -4.9736, img("1536728033382-9963497c2a99")),
        UnescoHeritageEntity("u-marrakech", "Medina of Marrakech", "Red walls, souks and palaces.", 1985, "marrakech", 31.6295, -7.9811, img("1510414842594-a61c69b5ae57")),
        UnescoHeritageEntity("u-ait", "Ksar of Ait Ben Haddou", "Earthen architecture of the south.", 1987, "ouarzazate", 31.0470, -7.1319, img("1489493173502-14ec3a0c5bb2")),
        UnescoHeritageEntity("u-volubilis", "Archaeological Site of Volubilis", "Roman city with triumphal arch.", 1997, "meknes", 34.0742, -5.5553, img("1469856853081-bb74dfbb6cbd")),
        UnescoHeritageEntity("u-essaouira", "Medina of Essaouira", "18th-century fortified port town.", 2001, "essaouira", 31.5085, -9.7595, img("1519046904884-53103b34b206"))
    )

    private fun events() = listOf(
        EventEntity("e-ramadan", "Ramadan Nights", "Festive evenings in medinas.", "cultural", "March-April", "April", "marrakech", 31.6295, -7.9811, img("1510414842594-a61c69b5ae57")),
        EventEntity("e-date-festival", "Erfoud Date Festival", "Harvest celebration in the Sahara.", "seasonal", "October", "October", "merzouga", 31.4315, -4.2326, img("1489493173502-14ec3a0c5bb2")),
        EventEntity("e-sufi", "Sufi Music Festival", "Sacred music in Fès.", "music", "April", "April", "fes", 34.0181, -5.0078, img("1536728033382-9963497c2a99"))
    )

    private fun circuits() = listOf(
        CircuitEntity("cir-imperial", "Imperial Cities Circuit", "Rabat, Meknès, Fès and Marrakech.", "imperial", 7, img("1536728033382-9963497c2a99")),
        CircuitEntity("cir-desert", "Desert Circuit", "Ouarzazate, Dades, Todra and Merzouga.", "desert", 4, img("1489493173502-14ec3a0c5bb2")),
        CircuitEntity("cir-gastro", "Gastronomy Circuit", "Taste Morocco from Fès to Marrakech.", "gastronomy", 5, img("1510414842594-a61c69b5ae57")),
        CircuitEntity("cir-culture", "Culture Circuit", "Museums, medinas and music.", "culture", 6, img("1536728033382-9963497c2a99")),
        CircuitEntity("cir-atlantic", "Atlantic Coast Circuit", "Tangier, Asilah, Rabat, Essaouira, Agadir.", "coast", 8, img("1519046904884-53103b34b206")),
        CircuitEntity("cir-mountains", "Mountains Circuit", "Ifrane, Azrou, Midelt and Atlas.", "mountains", 5, img("1506905925346-be21c11fd8de"))
    )

    private fun circuitSteps() = listOf(
        step("cs1", "cir-imperial", 1, "Rabat", "Capital and Hassan Tower.", "city", "rabat", 34.0209, -6.8416),
        step("cs2", "cir-imperial", 2, "Meknès", "Bab Mansour and royal stables.", "city", "meknes", 33.8935, -5.5473),
        step("cs3", "cir-imperial", 3, "Fès", "Medina and tanneries.", "city", "fes", 34.0181, -5.0078),
        step("cs4", "cir-imperial", 4, "Marrakech", "Jemaa el-Fna and palaces.", "city", "marrakech", 31.6295, -7.9811),
        step("cs5", "cir-desert", 1, "Ouarzazate", "Film studios and kasbahs.", "city", "ouarzazate", 30.9335, -6.9370),
        step("cs6", "cir-desert", 2, "Ait Ben Haddou", "UNESCO ksar.", "monument", "ait-ben-haddou", 31.0470, -7.1319),
        step("cs7", "cir-desert", 3, "Merzouga", "Erg Chebbi dunes.", "city", "merzouga", 31.0802, -4.0141)
    )

    private fun step(id: String, circuitId: String, order: Int, title: String, desc: String, refType: String, refId: String, lat: Double, lng: Double) =
        CircuitStepEntity(id, circuitId, order, title, desc, refType, refId, lat, lng)

    private fun tourismOffices() = listOf(
        TourismOfficeEntity("to-rabat", "ONMT Rabat", "rabat", "+212537268700", "info@visitmorocco.com", "https://www.visitmorocco.com", "Avenue Mohammed V", 34.0209, -6.8416),
        TourismOfficeEntity("to-marrakech", "CRT Marrakech", "marrakech", "+212524388740", "info@marrakech.ma", "https://www.visitmarrakech.com", "Place Abdelmoumen", 31.6295, -7.9811),
        TourismOfficeEntity("to-fes", "CRT Fès", "fes", "+212535622680", "info@fesmorocco.org", "https://www.fesmorocco.org", "Boulevard Mohammed V", 34.0181, -5.0078)
    )

    private fun recommendations() = listOf(
        RecommendationEntity("rec1", "Explore Chefchaouen", "Perfect for photography lovers.", "city", "chefchaouen", 9.5, "Based on popularity"),
        RecommendationEntity("rec2", "Sunset at Erg Chebbi", "Unforgettable desert experience.", "nature", "n-erg-chebbi", 9.8, "Top rated nature site"),
        RecommendationEntity("rec3", "Gnaoua Festival", "Plan your June trip to Essaouira.", "festival", "f-gnaoua", 9.2, "Seasonal highlight")
    )

    private fun placesFromContent(cities: List<CityEntity>, monuments: List<MonumentEntity>) =
        cities.map { c ->
            PlaceEntity(
                id = c.id,
                name = c.name,
                description = c.description,
                category = "CITY",
                latitude = c.latitude,
                longitude = c.longitude,
                imageUrl = c.imageUrl,
                rating = c.rating,
                address = c.name,
                placeType = "city",
                cachedAt = System.currentTimeMillis()
            )
        } + monuments.map { m ->
            PlaceEntity(
                id = m.id,
                name = m.name,
                description = m.description,
                category = "CULTURE",
                latitude = m.latitude,
                longitude = m.longitude,
                imageUrl = m.imageUrl,
                rating = m.rating,
                address = m.name,
                placeType = m.monumentType,
                cachedAt = System.currentTimeMillis()
            )
        }

    private fun buildContentIndex(cities: List<CityEntity>, monuments: List<MonumentEntity>): List<ContentIndexEntity> {
        val index = mutableListOf<ContentIndexEntity>()
        cities.forEach { c ->
            index += ContentIndexEntity(
                id = "city-${c.id}", contentType = "CITY", title = c.name,
                subtitle = c.climate, description = c.description, imageUrl = c.imageUrl,
                latitude = c.latitude, longitude = c.longitude, regionId = c.regionId,
                cityId = c.id, category = "CITY", rating = c.rating, popularity = c.popularity
            )
        }
        monuments.forEach { m ->
            index += ContentIndexEntity(
                id = "monument-${m.id}", contentType = "MONUMENT", title = m.name,
                subtitle = m.monumentType, description = m.description, imageUrl = m.imageUrl,
                latitude = m.latitude, longitude = m.longitude, cityId = m.cityId,
                category = "MONUMENT", rating = m.rating, popularity = m.popularity,
                priceLabel = m.entryPrice
            )
        }
        cultureItems().forEach { c ->
            index += ContentIndexEntity("culture-${c.id}", "CULTURE", c.title, c.cultureType, c.description, c.imageUrl, regionId = c.regionId, category = "CULTURE", popularity = 70)
        }
        architectureItems().forEach { a ->
            index += ContentIndexEntity("arch-${a.id}", "ARCHITECTURE", a.title, a.architectureType, a.description, a.imageUrl, regionId = a.regionId, category = "ARCHITECTURE", popularity = 65)
        }
        musicStyles().forEach { m ->
            index += ContentIndexEntity("music-${m.id}", "MUSIC", m.name, m.regions, m.description, m.imageUrl, category = "MUSIC", popularity = 60)
        }
        dishes().forEach { d ->
            index += ContentIndexEntity("dish-${d.id}", "GASTRONOMY", d.name, d.dishType, d.description, d.imageUrl, regionId = d.regionId, category = "GASTRONOMY", rating = d.rating, popularity = 75)
        }
        localProducts().forEach { p ->
            index += ContentIndexEntity("product-${p.id}", "PRODUCT", p.name, "", p.description, p.imageUrl, regionId = p.regionId, category = "GASTRONOMY", popularity = 55)
        }
        festivals().forEach { f ->
            index += ContentIndexEntity("festival-${f.id}", "FESTIVAL", f.name, f.dates, f.description, f.imageUrl, latitude = f.latitude, longitude = f.longitude, cityId = f.cityId, category = "FESTIVAL", popularity = 80)
        }
        artisanatItems().forEach { a ->
            index += ContentIndexEntity("artisanat-${a.id}", "ARTISANAT", a.name, a.craftType, a.description, a.imageUrl, regionId = a.regionId, category = "ARTISANAT", popularity = 50)
        }
        natureSites().forEach { n ->
            index += ContentIndexEntity("nature-${n.id}", "NATURE", n.name, n.natureType, n.description, n.imageUrl, latitude = n.latitude, longitude = n.longitude, regionId = n.regionId, category = "NATURE", rating = n.rating, popularity = 85)
        }
        activities().forEach { a ->
            index += ContentIndexEntity("activity-${a.id}", "ACTIVITY", a.name, a.activityType, a.description, a.imageUrl, latitude = a.latitude, longitude = a.longitude, regionId = a.regionId, category = "ACTIVITY", rating = a.rating, popularity = 78, priceLabel = a.priceRange)
        }
        museums().forEach { m ->
            index += ContentIndexEntity("museum-${m.id}", "MUSEUM", m.name, m.cityId ?: "", m.description, m.imageUrl, latitude = m.latitude, longitude = m.longitude, cityId = m.cityId, category = "MUSEUM", rating = m.rating, popularity = 72, priceLabel = m.entryPrice)
        }
        unescoSites().forEach { u ->
            index += ContentIndexEntity("unesco-${u.id}", "UNESCO", u.name, "UNESCO ${u.yearInscribed}", u.description, u.imageUrl, latitude = u.latitude, longitude = u.longitude, cityId = u.cityId, category = "UNESCO", popularity = 90)
        }
        events().forEach { e ->
            index += ContentIndexEntity("event-${e.id}", "EVENT", e.name, e.eventType, e.description, e.imageUrl, latitude = e.latitude, longitude = e.longitude, cityId = e.cityId, category = "EVENT", popularity = 65)
        }
        circuits().forEach { c ->
            index += ContentIndexEntity("circuit-${c.id}", "CIRCUIT", c.name, "${c.durationDays} days", c.description, c.imageUrl, category = "CIRCUIT", popularity = 88)
        }
        return index
    }
}
