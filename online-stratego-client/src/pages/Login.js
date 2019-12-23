import React, { Component } from 'react';
import { Form } from 'react-bootstrap';
import Button from 'react-bootstrap/Button';

const restUrl = 'http://localhost:9000';

class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            user: null
        };

        // Bindings
        this.handleLogin = this.handleLogin.bind(this);
        this.handleRegister = this.handleRegister.bind(this);
        this.handleChangeUsername = this.handleChangeUsername.bind(this);
        this.handleChangePassword = this.handleChangePassword.bind(this);
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevState !== this.state) {
            console.log(this.state);
        }
    }

    handleErrors(response) {
        if (!response.ok) {
            throw Error(response.statusText);
        }
        return response;
    }

    handleLogin() {
        let url = [];
        url.push(
            restUrl,
            '/login?',
            'username=',
            this.state.username,
            '&password=',
            this.state.password
        );
        url = url.join('');
        console.log(url);

        fetch(url)
            .then(res => res.json)
            .then((data) => {
                this.setState({
                    user: data
                }, () => {
                    console.log(this.state.user, 'user');
                    if (this.state.user.id !== null){
                        localStorage.setItem('user', this.state.user);
                        //window.location = '/lobby';
                    } else {
                        window.alert('Could not find account with those credentials!');
                    }
                })
            })
    }

    handleRegister() {
        window.location = '/register';
    }

    handleChangeUsername(e) {
        this.setState({
            username: e.target.value
        });
    }

    handleChangePassword(e) {
        this.setState({
            password: e.target.value
        });
    }

    render() {
        return (
            <div className='Layout'>
                <header className='Header'>
                    Online Stratego
            </header>

                <Form className='Form' style={{ backgroundColor: 'rgba(235,200,70,0.9)' }}>
                    <Form.Group>
                        <Form.Control className='input' type='text' id='username' placeholder='Username' onChange={this.handleChangeUsername} />
                    </Form.Group>
                    <Form.Group>
                        <Form.Control className='input' type='password' id='password' placeholder='Password' onChange={this.handleChangePassword} />
                    </Form.Group>
                    <Form.Group>
                        <Button className='btn btn-warning' onClick={this.handleLogin} >
                            Login
                        </Button>
                        <Button className='btn btn-warning' onClick={this.handleRegister} >
                            Register new account
                        </Button>
                    </Form.Group>
                </Form>
            </div >

        )
    }
}
export default Login;