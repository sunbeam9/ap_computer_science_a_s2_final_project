import { customElement, html } from 'lit-element';
import { View } from '../../views/view';

@customElement('how-to-play-view')
export class HowToPlayView extends View {
  render() {
    return html`
<div>
<h1 style="text-align: center;">How To Play</h1>
<div style="text-align: center; padding-left: 10em; padding-right: 10em;">
<p>Evil Hangman is similar to the classic and beloved game of Hangman with one notable exception: rather than choosing a single word, the game will select a word length from a dictionary. This dictionary will be continuously updated to dodge your guesses.</p>
<p>You can play Evil Hangman the same way that you would play Hangman. Just enter you guess into the text field and click the "Guess" button.</p>
</div>
</div>
`;
  }
}
