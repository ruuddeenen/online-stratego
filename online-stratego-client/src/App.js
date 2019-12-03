import React from 'react';
import './css/App.css';
import './css/bootstrap.css'
import './css/bootstrap-grid.css'
import { BrowserRouter, Route } from 'react-router-dom';
import Game from './pages/Game'

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
