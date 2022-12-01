package com.example.booksapi;

import static com.example.booksapi.BookDetails.EXTRA_BOOK_OBJ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String IMAGE_URL = "http://covers.openlibrary.org/b/id/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.book_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchBooksData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void fetchBooksData(String query) {
        String finalQuery = prepareQuery(query);
        BookService bookService = RetrofitInstance.getRetrofitInstance().create(BookService.class);
        Call<BookContainer> booksApiCall = bookService.findBooks(finalQuery);

        booksApiCall.enqueue(new Callback<BookContainer>() {
            @Override
            public void onResponse(Call<BookContainer> call, Response<BookContainer> response) {
                if (response.body() != null)
                    setupBookListView(response.body().getBookList());
            }

            @Override
            public void onFailure(Call<BookContainer> call, Throwable t) {
                Snackbar.make(findViewById(R.id.main_view), getResources().getString(R.string.fail_message),
                                Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private String prepareQuery(String query) {
        String[] queryParts = query.split("\\s+");
        return TextUtils.join("+", queryParts);
    }

    private void setupBookListView(List<Book> books) {
        RecyclerView view = findViewById(R.id.recyclerview);
        BookAdapter bookAdapter = new BookAdapter();
        bookAdapter.setBooks(books);
        view.setAdapter(bookAdapter);
        view.setLayoutManager(new LinearLayoutManager(this));
    }

    private class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookHolder> {
        private List<Book> books;

        @NonNull
        @Override
        public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflate = LayoutInflater.from(parent.getContext());
            return new BookHolder(inflate, parent);
        }

        public void onBindViewHolder(@NonNull BookHolder holder, int position) {
            if (books != null) {
                Book book = books.get(position);
                holder.bind(book);
            } else {
                Log.d("MainActivity", "No books");
            }
        }

        public void setBooks(List<Book> books) {
            this.books = books;
            notifyDataSetChanged();
        }


        @Override
        public int getItemCount() {
            return books.size();
        }


        private class BookHolder extends RecyclerView.ViewHolder {


            private TextView bookTitleTextView;
            private TextView bookAuthorTextView;
            private TextView numberOfPagesTextView;
            private ImageView bookCover;

            public BookHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.book_list, parent, false));
                bookTitleTextView = itemView.findViewById(R.id.book_title);
                bookAuthorTextView = itemView.findViewById(R.id.book_author);
                numberOfPagesTextView = itemView.findViewById(R.id.number_of_pages);
                bookCover = itemView.findViewById(R.id.img_cover);
            }
            String cover;
            public void bind(Book book) {  /////////////////////////////////////here is binded a listener to element
                if (book != null && checkNullOrEmpty(book.getTitle()) && book.getAuthors() != null) {
                    bookTitleTextView.setText(book.getTitle());
                    bookAuthorTextView.setText(TextUtils.join(", ", book.getAuthors()));
                    numberOfPagesTextView.setText(book.getNumberOfPages());

                    View itemContainer = itemView.findViewById(R.id.book_item_container);
                    itemContainer.setOnClickListener(v -> {
                        Intent intent = new Intent(MainActivity.this, BookDetails.class);  //open an element by clicking on it
//                        intent.putExtra(EXTRA_BOOK_OBJ, "dsadasd");
                        if(book.getCover() == null){ cover = "empty"; }
                        else cover = book.getCover();
                        intent.putExtra(EXTRA_BOOK_OBJ, "authors:" + book.getAuthors()+";" +
                                "title:"+ book.getTitle() + ";type:" + book.getType() + ";languages:" + book.getLanguages() +
                                ";cover:" + cover);
                        startActivity(intent);
                    });

                    if (book.getCover() != null) {
                        Picasso.with(itemView.getContext())
                                .load(IMAGE_URL + book.getCover() + "-S.jpg")
                                .placeholder(R.drawable.book).into(bookCover);
                    } else {
                        bookCover.setImageResource(R.drawable.book);
                    }
                }
            }

            private boolean checkNullOrEmpty(String s) {
                return s != null & !s.isEmpty();
            }
        }
    }
}