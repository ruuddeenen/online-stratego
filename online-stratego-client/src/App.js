import React from 'react';
import './css/App.css';
import './css/bootstrap.css'
import './css/bootstrap-grid.css'
import { BrowserRouter, Route } from 'react-router-dom';
import Game from './pages/Game';
import Menu from './pages/Menu';
import Lobby from './pages/Lobby';

function App() {
  return (
    <BrowserRouter>
      <div className='App'>
        <Route path='/' exact component={Menu} />
        <Route path='/game' exact component={Game} />
        <Route path='/lobby' exact component={Lobby}/>
      </div>
    </BrowserRouter>
  );
}
export default App;
