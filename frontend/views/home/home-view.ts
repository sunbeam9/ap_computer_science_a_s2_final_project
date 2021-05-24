import { customElement, html } from 'lit-element';
import { View } from '../../views/view';

@customElement('home-view')
export class HomeView extends View {
  render() {
    return html`
<div>
 <h1 style="text-align: center; margin-bottom: 0.5em;">Evil Hangman</h1>
 <h3 style="text-align: center; margin-top: 0px; margin-bottom: 2em;">by Anshul Noori</h3>
 <div style="text-align: center; padding-left: 10em; padding-right: 10em;">
 <p>Visit the <a href="/play">Play page</a> to start playing Evil Hangman!</p>
 <p>Not sure how to play? Visit the <a href="/how-to-play">How To Play page</a> to learn how to play Evil Hangman.</p>
 </div>
</div>
`;
  }
}
