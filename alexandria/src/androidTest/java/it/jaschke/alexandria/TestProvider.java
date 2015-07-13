package it.jaschke.alexandria;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.jaschke.alexandria.data.AlexandriaContract;

import static org.assertj.android.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by saj on 23/12/14.
 */
@RunWith(AndroidJUnit4.class)
public class TestProvider {
    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    @Before
    public void setUp() {
        deleteAllRecords();
    }

    public void deleteAllRecords() {
        InstrumentationRegistry.getTargetContext().getContentResolver().delete(
                AlexandriaContract.BookEntry.CONTENT_URI,
                null,
                null
        );
        InstrumentationRegistry.getTargetContext().getContentResolver().delete(
                AlexandriaContract.CategoryEntry.CONTENT_URI,
                null,
                null
        );

        InstrumentationRegistry.getTargetContext().getContentResolver().delete(
                AlexandriaContract.AuthorEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = InstrumentationRegistry.getTargetContext().getContentResolver().query(
                AlexandriaContract.BookEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertThat(cursor).hasCount(0);
        cursor.close();

        cursor = InstrumentationRegistry.getTargetContext().getContentResolver().query(
                AlexandriaContract.AuthorEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertThat(cursor).hasCount(0);
        cursor.close();

        cursor = InstrumentationRegistry.getTargetContext().getContentResolver().query(
                AlexandriaContract.CategoryEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertThat(cursor).hasCount(0);
        cursor.close();
    }

    @Test
    public void testGetType() {

        String type = InstrumentationRegistry.getTargetContext()
                .getContentResolver()
                .getType(AlexandriaContract.BookEntry.CONTENT_URI);
        assertThat(AlexandriaContract.BookEntry.CONTENT_TYPE).isEqualTo(type);

        type = InstrumentationRegistry.getTargetContext()
                .getContentResolver()
                .getType(AlexandriaContract.AuthorEntry.CONTENT_URI);
        assertThat(AlexandriaContract.AuthorEntry.CONTENT_TYPE).isEqualTo(type);

        type = InstrumentationRegistry.getTargetContext()
                .getContentResolver()
                .getType(AlexandriaContract.CategoryEntry.CONTENT_URI);
        assertThat(AlexandriaContract.CategoryEntry.CONTENT_TYPE).isEqualTo(type);

        long id = 9780137903955L;
        type = InstrumentationRegistry.getTargetContext()
                .getContentResolver()
                .getType(AlexandriaContract.BookEntry.buildBookUri(id));
        assertThat(AlexandriaContract.BookEntry.CONTENT_ITEM_TYPE).isEqualTo(type);

        type = InstrumentationRegistry.getTargetContext()
                .getContentResolver()
                .getType(AlexandriaContract.BookEntry.buildFullBookUri(id));
        assertThat(AlexandriaContract.BookEntry.CONTENT_ITEM_TYPE).isEqualTo(type);

        type = InstrumentationRegistry.getTargetContext()
                .getContentResolver()
                .getType(AlexandriaContract.AuthorEntry.buildAuthorUri(id));
        assertThat(AlexandriaContract.AuthorEntry.CONTENT_ITEM_TYPE).isEqualTo(type);

        type = InstrumentationRegistry.getTargetContext()
                .getContentResolver()
                .getType(AlexandriaContract.CategoryEntry.buildCategoryUri(id));
        assertThat(AlexandriaContract.CategoryEntry.CONTENT_ITEM_TYPE).isEqualTo(type);

    }

    @Test
    public void testInsertRead() {

        insertReadBook();
        insertReadAuthor();
        insertReadCategory();

        readFullBook();
        readFullList();
    }

    public void insertReadBook() {
        ContentValues bookValues = TestDb.getBookValues();

        Uri bookUri = InstrumentationRegistry.getTargetContext()
                .getContentResolver()
                .insert(AlexandriaContract.BookEntry.CONTENT_URI, bookValues);
        long bookRowId = ContentUris.parseId(bookUri);
        assertThat(bookRowId).isNotEqualTo(-1);

        Cursor cursor = InstrumentationRegistry.getTargetContext().getContentResolver()
                .query(
                        AlexandriaContract.BookEntry.CONTENT_URI,
                        null, // leaving "columns" null just returns all the columns.
                        null, // cols for "where" clause
                        null, // values for "where" clause
                        null  // sort order
                );

        TestDb.validateCursor(cursor, bookValues);

        cursor = InstrumentationRegistry.getTargetContext().getContentResolver()
                .query(
                        AlexandriaContract.BookEntry.buildBookUri(bookRowId),
                        null, // leaving "columns" null just returns all the columns.
                        null, // cols for "where" clause
                        null, // values for "where" clause
                        null  // sort order
                );

        TestDb.validateCursor(cursor, bookValues);

    }

    public void insertReadAuthor() {
        ContentValues authorValues = TestDb.getAuthorValues();

        Uri authorUri = InstrumentationRegistry.getTargetContext()
                .getContentResolver()
                .insert(AlexandriaContract.AuthorEntry.CONTENT_URI, authorValues);
        long authorRowId = ContentUris.parseId(authorUri);
        assertThat(authorRowId).isNotEqualTo(-1);
        assertThat(authorRowId).isEqualTo(TestDb.ean);

        Cursor cursor = InstrumentationRegistry.getTargetContext().getContentResolver().query(
                AlexandriaContract.AuthorEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestDb.validateCursor(cursor, authorValues);

        cursor = InstrumentationRegistry.getTargetContext().getContentResolver().query(
                AlexandriaContract.AuthorEntry.buildAuthorUri(authorRowId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestDb.validateCursor(cursor, authorValues);

    }

    public void insertReadCategory() {
        ContentValues categoryValues = TestDb.getCategoryValues();

        Uri categoryUri = InstrumentationRegistry.getTargetContext().getContentResolver().insert(AlexandriaContract.CategoryEntry.CONTENT_URI, categoryValues);
        long categoryRowId = ContentUris.parseId(categoryUri);
        assertThat(categoryRowId).isNotEqualTo(-1);
        assertThat(categoryRowId).isEqualTo(TestDb.ean);

        Cursor cursor = InstrumentationRegistry.getTargetContext().getContentResolver().query(
                AlexandriaContract.CategoryEntry.CONTENT_URI,
                null, // projection
                null, // selection
                null, // selection args
                null  // sort order
        );

        TestDb.validateCursor(cursor, categoryValues);

        cursor = InstrumentationRegistry.getTargetContext().getContentResolver().query(
                AlexandriaContract.CategoryEntry.buildCategoryUri(categoryRowId),
                null, // projection
                null, // selection
                null, // selection args
                null  // sort order
        );

        TestDb.validateCursor(cursor, categoryValues);

    }

    public void readFullBook() {

        Cursor cursor = InstrumentationRegistry.getTargetContext().getContentResolver().query(
                AlexandriaContract.BookEntry.buildFullBookUri(TestDb.ean),
                null, // projection
                null, // selection
                null, // selection args
                null  // sort order
        );

        TestDb.validateCursor(cursor, TestDb.getFullDetailValues());
    }

    public void readFullList() {

        Cursor cursor = InstrumentationRegistry.getTargetContext().getContentResolver().query(
                AlexandriaContract.BookEntry.FULL_CONTENT_URI,
                null, // projection
                null, // selection
                null, // selection args
                null  // sort order
        );

        TestDb.validateCursor(cursor, TestDb.getFullListValues());
    }


}