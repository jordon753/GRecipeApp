package com.gordon.grecipeapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.gordon.grecipeapp.Database.GRecipeDatabase
import com.gordon.grecipeapp.Database.Recipe
import com.gordon.grecipeapp.Database.RecipeDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals


@RunWith(AndroidJUnit4::class)
@SmallTest
class RecipeDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: GRecipeDatabase
    private lateinit var recipeDao: RecipeDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GRecipeDatabase::class.java
        ).allowMainThreadQueries().build()
        recipeDao = database.recipeDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertRecipe_savesCorrectly() = runTest {
        val recipe = Recipe(title = "Test", description = "desc", category = "dessert")
        recipeDao.insertRecipe(recipe)

        val allRecipes = recipeDao.getAllRecipes().first()
        assertEquals(1, allRecipes.size)
        assertEquals("Test", allRecipes[0].title)
    }

    @Test
    fun deleteRecipe_removesCorrectly() = runTest {
        val recipe = Recipe(title = "Delete", description = "desc", category = "main")
        recipeDao.insertRecipe(recipe)

        val inserted = recipeDao.getAllRecipes().first()[0]
        recipeDao.deleteRecipe(inserted)

        val remaining = recipeDao.getAllRecipes().first()
        assertEquals(0, remaining.size)
    }
}
