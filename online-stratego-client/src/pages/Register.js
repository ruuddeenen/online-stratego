import React, { Component } from 'react';
import { Form } from 'react-bootstrap';
import Button from 'react-bootstrap/Button';
import { createUrlToRestApi } from '../scripts/RestHandler';

class Register extends Component {

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
        this.handleChangePasswordConf = this.handleChangePasswordConf.bind(this);
        this.toLogin = this.toLogin.bind(this);
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
        window.location = '/';
    }

    handleRegister() {
        const username = this.state.username;
        const password = this.state.password;
        const passwordConfirmation = this.state.passwordConfirmation;
        
        if (password !== passwordConfirmation){
            window.alert('Passwords do not match.');
            return;
        }

        if (username === 'null') {
            window.alert('Username cannot be null.');
            return;
        }

        const url = createUrlToRestApi('register', ['username', 'password'], [username, password]);

        fetch(url)
            .then(res => res.json())
            .then((data) => {
                console.log(data, 'data');
                if (data.id !== 'null' || data.username !== 'null') {
                    window.location = '/';
                } else {
                    window.alert('Could not create account! Unkown error.');
                }
            });
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

    handleChangePasswordConf(e) {
        this.setState({
            passwordConfirmation: e.target.value
        });
    }

    toLogin() {
        window.location = '/';
    }

    render() {
        return (
            <div className='Layout'>
                <header className='Header'>
                    Online Stratego
            </header>

                <Form className='Form' style={{ backgroundColor: 'rgba(235,200,70,0.9)' }}>
                    <Form.Group>
                        <Button className='btn btn-warning' onClick={this.toLogin}>
                            Back to login
                        </Button>
                    </Form.Group>
                    <Form.Group>
                        <Form.Control className='input' type='text' id='username' placeholder='Username' onChange={this.handleChangeUsername} />
                    </Form.Group>
                    <Form.Group>
                        <Form.Control className='input' type='password' id='password' placeholder='Password' autoComplete='new-password' onChange={this.handleChangePassword} />
                    </Form.Group>
                    <Form.Group>
                        <Form.Control className='input' type='password' id='passwordConf' placeholder='Confirm password' autoComplete='new-password' onChange={this.handleChangePasswordConf} />
                    </Form.Group>
                    <Form.Group>
                        <Button className='btn btn-warning' onClick={this.handleRegister} >
                            Create account
                        </Button>
                    </Form.Group>
                </Form>
            </div >

        )
    }
}
export default Register;