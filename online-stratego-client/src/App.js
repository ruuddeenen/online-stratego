import React from 'react';
import './css/bootstrap.css'
import './css/bootstrap-grid.css'
import './css/App.css';
import { BrowserRouter, Route } from 'react-router-dom';
import Game from './pages/Game';
import Login from './pages/Login';
import Lobby from './pages/Lobby';
import Register from './pages/Register';
import JoinLobby from './pages/joinlobby';

function App() {
  return (
    <BrowserRouter>
      <div className='App'>
        <Route path='/' exact component={Login} />
        <Route path='/register' exact component={Register}/>
        <Route path='/game' exact component={Game} />
        <Route path='/lobby' exact component={Lobby}/>
        <Route path='/joinlobby' exact component={JoinLobby}/>
      </div>
    </BrowserRouter>
  );
}
export default App;
