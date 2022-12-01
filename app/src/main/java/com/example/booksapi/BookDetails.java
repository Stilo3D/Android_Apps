package com.example.booksapi;

import static com.example.booksapi.MainActivity.IMAGE_URL;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class BookDetails extends AppCompatActivity {
    public static String EXTRA_BOOK_OBJ = "EXTRA_BOOK_OBJ";

    private TextView bookTitleTextView, bookAuthorTextView, bookLanguagesTextView, bookTypeTextView;
    private ImageView bookCover;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_details);
        bookTitleTextView = findViewById(R.id.book_title);
        bookAuthorTextView = findViewById(R.id.book_author);
        bookLanguagesTextView = findViewById(R.id.book_languages);
        bookTypeTextView = findViewById(R.id.book_type);

        bookCover = findViewById(R.id.img_cover);

        String book = (String) getIntent().getSerializableExtra(EXTRA_BOOK_OBJ);
        book = book.replaceAll("\\[" ,"");
        book = book.replaceAll("\\]", "");

        String[] bookTab = book.split(";");

        bookTitleTextView.setText("Title: " + bookTab[1].split(":")[1]);
        bookAuthorTextView.setText("Author(s): " + bookTab[0].split(":")[1]);
        bookLanguagesTextView.setText("Languages: " + bookTab[3].split(":")[1]);
        bookTypeTextView.setText("Type: " + bookTab[2].split(":")[1]);

        if (!bookTab[4].split(":")[1].equals("empty")) {
            Picasso.with(getApplicationContext())
                    .load(IMAGE_URL + bookTab[4].split(":")[1] + "-L.jpg")
                    .placeholder(R.drawable.book).into(bookCover);
        } else {
            bookCover.setImageResource(R.drawable.book_big);
        }

    }

}
