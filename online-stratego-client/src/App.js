import React from 'react';
import './App.css';
import { BrowserRouter, Route } from 'react-router-dom';
import {Game} from './pages/game.js'

function App() {
  return (
    <BrowserRouter>
      <div className='App'>
        <Route path='/' exact component={Game} />
      </div>
    </BrowserRouter>
  );
}

export default App;
