/**
 * 
 */

import {PolymerElement,html} from '@polymer/polymer/polymer-element.js';
import tinymce from 'tinymce/tinymce.js';


class MainPage extends PolymerElement {

    static get template() {
        return html`
            <div id="header">Main page</div>
            <div id="content"></div>
            <hr>
            <div id="footer">
                <vaadin-text-area id="textarea">yolo<vaadin-text-area/>
            </div>`;
    }

    static get is() {
          return 'main-page';
    }
}

customElements.define(MainPage.is, MainPage);


