import React, { Component } from "react";
import { Button, Form } from "react-bootstrap";

class JoinLobby extends Component {
    constructor(props){
        super(props);
        this.state = {
            user: null,
            lobbyId: ''
        }
        

        // Bindings
        this.changeLobbyId = this.changeLobbyId.bind(this);
        this.createLobby = this.createLobby.bind(this);
        this.joinLobby = this.joinLobby.bind(this);
    }

    componentDidMount() {
        this.getUser();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        console.log(this.state, 'state');
    }

    getUser(){
        this.setState({
            user: JSON.parse(sessionStorage.getItem('user'))
        });
    }

    changeLobbyId(e){
        this.setState({
            lobbyId: e.target.value.toUpperCase()
        });
    }

    createLobby(){
        sessionStorage.setItem('lobbyId', '');
        sessionStorage.setItem('color', '');
        window.location = '/lobby';
    }

    joinLobby(){
        const lobbyId = this.state.lobbyId;
        if (lobbyId.length !== 5){
            window.alert('Lobby ID must be 5 characters!');
            return;
        }
        sessionStorage.setItem('lobbyId', this.state.lobbyId);
        window.location = '/lobby';
    }

    render() {
        return (
            <div className='Layout'>
                <header className='Header'>Create or join lobby</header>
                <div className='container'>
                    <div className='row'>
                        <div className='col-sm'>
                            <Button className='btn btn-warning' onClick={this.createLobby}>Create new lobby</Button>
                        </div>
                        <div className='col-sm'>
                            <Form.Group>
                                <Button className='btn btn-warning' onClick={this.joinLobby}>Join a private lobby</Button>
                                <Form.Control className='input upper-case' type='text' id='lobbyId' maxLength='5' placeholder='Lobby ID' onChange={this.changeLobbyId} />
                            </Form.Group>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default JoinLobby;