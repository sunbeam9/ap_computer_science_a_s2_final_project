package com.example.application.views.play;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Collections;

@Route(value = "play")
@PageTitle("Play")
@CssImport("./styles/play-view.css")
public class PlayView extends Div {

    EvilHangman evilHangman;

    H4 guessesTitle = new H4("Your Guesses:");
    UnorderedList guessesList = new UnorderedList();
    H4 wordPattern = new H4();
    H4 numberOfGuesses = new H4();
    VerticalLayout gameElements = new VerticalLayout(guessesTitle, guessesList, wordPattern, numberOfGuesses);

    TextField guessField = new TextField();
    Button guessButton = new Button("Guess");
    HorizontalLayout inputs = new HorizontalLayout(guessField, guessButton);

    Button playAgain = new Button("Play Again");

    public PlayView() {
        addClassName("play-view");
        initializeGame();

        this.addListener(GameOverEvent.class, gameOverEvent -> {
            if (!evilHangman.getWordPattern().contains("_")) {
                Notification.show("You Won!");
            } else {
                wordPattern.setText(evilHangman.getRandomWord());
                Notification.show("You Lose. The word was " + wordPattern.getText());
            }
            guessButton.setEnabled(false);
            guessField.setEnabled(false);
            playAgain.setEnabled(true);
            playAgain.setVisible(true);
        });

        add(gameElements, inputs, playAgain);
    }

    public void initializeGame() {
        playAgain.setEnabled(false);
        playAgain.setVisible(false);
        playAgain.addClickListener(click -> initializeGame());

        evilHangman = new EvilHangman();

        guessesList.removeAll();
        guessesList.setId("guessesList");

        wordPattern.setText(evilHangman.getWordPattern());

        numberOfGuesses.setText("Number Of Guesses Left: " + evilHangman.getNumberOfGuesses());

        initializeInputs();
    }

    public void initializeInputs() {
        guessField.setEnabled(true);
        guessField.setPlaceholder("Your Guess");
        guessField.setMaxLength(1);
        guessField.setValue("");

        guessButton.setEnabled(true);
        guessButton.addClickShortcut(Key.ENTER);
        guessButton.addClickListener(click -> {
            String guess = guessField.getValue().toLowerCase();
            guessField.setValue("");

            if (guess.equalsIgnoreCase("")) {
                Notification.show("You cannot guess a blank letter.");
                return;
            }

            if (guess.length() != 1 || evilHangman.getGuesses().contains(guess)) {
                Notification.show("You have already guessed that letter. Please try again with a different letter.");
                return;
            }
            if (guess.toCharArray()[0] <= 96 || guess.toCharArray()[0] >= 123) {
                Notification.show("You have entered an invalid guess. Please try again with a different guess.");
                return;
            }

            int numChanges = evilHangman.newGuess(guess);

            Notification.show("Your guess revealed " + numChanges + " letters.");

            update();

            if (evilHangman.getNumberOfGuesses() == 0 || !evilHangman.getWordPattern().contains("_")) {
                fireEvent(new GameOverEvent(this, false));
            }
        });
    }

    public void update() {
        wordPattern.setText(evilHangman.getWordPattern());
        numberOfGuesses.setText("Number Of Guesses Left: " + evilHangman.getNumberOfGuesses());

        guessesList.removeAll();
        ArrayList<String> guesses = evilHangman.getGuesses();
        Collections.sort(guesses);
        guesses.forEach(value -> guessesList.add(new ListItem(value)));
    }

}
