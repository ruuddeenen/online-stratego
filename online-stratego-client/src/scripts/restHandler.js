
const restUrl = 'http://localhost:9000';


export function createUrlToRestApi(location, parameters, values) {
    if (parameters.length !== values.length){
        return 'Parameter and values size does not match.'
    }

    let url = [];
    url.push(
        restUrl,
        '/',
        location,
        '?'
    );

    for (let i = 0; i < parameters.length; i++) {
        url.push(
            parameters[i],
            '=',
            values[i],
            '&'
        );
    };
    return url.join('');
}