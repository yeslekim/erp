// 공통 request send
async function common_fetch(method, url, dataset, callback) {
	let options = {
		method
		, headers : {
			'Content-Type': 'application/json'
			, Authorization : 'BEARER ' + window.localStorage.getItem("acTk")
			, 'fetch' : true
		}
	};
	if ( 'GET' === method ) {
		url += '?' + ( new URLSearchParams( dataset ) ).toString();
	} else {
		options.body = JSON.stringify( dataset );
	}
	
	await fetch( url, options )
		.then((data) => {
			return data.json()
		})
		.catch((err) => {
			console.log(err)
			sweetAlert("관리자에게 문의하세요.")
			return false;
		})
		// Promise 작동방식으로 인해 한번 더 then 후 callback
		.then(data => {
			callback(data)
		});

}

function sweetAlert(msg){
	swal(msg, {
		buttons: {
			confirm: {
				text  : '확인',
				className: 'btn btn-dark btn-flat'
			}
		},
	});
}

function sweetConfirmOkay(type, msg, callback) {
	swal({
		text: msg,
		icon: type, //success, warning, error, info
		buttons: {
			confirm: {
				text: '확인',
				className: 'btn btn-dark btn-flat'
			},
		}
	}).then((clicked) => {
		if (clicked) {
			callback();
		}
	});
}