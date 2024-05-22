package com.example.storyapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.data.StoriesRepository
import com.example.storyapp.data.local.database.story.StoryEntity
import com.example.storyapp.ui.adapter.StoriesAdapter
import com.example.storyapp.ui.home.utils.DataDummy
import com.example.storyapp.ui.home.utils.MainDispatcherRule
import com.example.storyapp.ui.home.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)

class HomeViewModelTest {

    @Mock
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var homeViewModel: HomeViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Before
    fun setup() {
        homeViewModel = HomeViewModel(storiesRepository)
    }

    @Test
    fun `when Get Stories Should Not Null and Return Expected Value`() = runTest {
        val dummyStory = DataDummy.generateDataStoryEntity()
        val expectedPagingStory = PagingData.from(dummyStory)
        val liveData = MutableLiveData<PagingData<StoryEntity>>()
        liveData.value = expectedPagingStory
        `when`(storiesRepository.getAllStories()).thenReturn(liveData)

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        val observedData = homeViewModel.loadStories().getOrAwaitValue()
        differ.submitData(observedData)

        val actualList = differ.snapshot()

        assertNotNull(observedData)
        assertEquals(dummyStory.size, actualList.size)

        val dummyFirstData = dummyStory[0]
        val actualFirstData = actualList[0]
        assertEquals(dummyFirstData.id, actualFirstData?.id)
        assertEquals(dummyFirstData.name, actualFirstData?.name)
        assertEquals(dummyFirstData.description, actualFirstData?.description)
        assertEquals(dummyFirstData.createdAt, actualFirstData?.createdAt)
        assertEquals(dummyFirstData.lat, actualFirstData?.lat)
        assertEquals(dummyFirstData.lon, actualFirstData?.lon)
        assertEquals(dummyFirstData.photoUrl, actualFirstData?.photoUrl)
    }

    @Test
    fun `when Get Stories is Empty Return 0`() = runTest {
        val expectedPagingStory: PagingData<StoryEntity> = PagingData.from(emptyList())
        val liveData = MutableLiveData<PagingData<StoryEntity>>()
        liveData.value = expectedPagingStory
        `when`(storiesRepository.getAllStories()).thenReturn(liveData)

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        val observedData = homeViewModel.loadStories().getOrAwaitValue()
        differ.submitData(observedData)

        val actualList = differ.snapshot()
        assertTrue(actualList.isEmpty())
        assertEquals(0, actualList.size)
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}

