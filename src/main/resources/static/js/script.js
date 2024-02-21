console.log("This is script file");

const toggleBar=()=>{
	if($(".sidebar").is(":visible"))
	{
		$(".sidebar").css("display","none");
		$(".content").css("margin-left","5px");
	}
	else{
		$(".sidebar").css("display","block");
		$(".content").css("margin-left","20%");
	}
}

const search = () =>{
	console.log("Searching..");

	let query = $("#search-input").val();
	console.log(query);
	if(query == '')
	{
		$(".search-result").hide();
	}
	else{
		let url = `http://localhost:9090/smartApp/contact-search/${query}`;

		fetch(url)
		.then(response => {
			return response.json();
		})
		.then((data) => {

			let text = `<div class="list-group" >`;

			data.forEach(contact=>{
				text+=`<a href="../view-contact/${contact.id}" class="list-group-item list-group-item-action">${contact.name}</a>`;
			})

			text+=`</div>`;

			$(".search-result").html(text);

			console.log(data);
		})

		$(".search-result").show();
	}
}

const startPayment=() =>{
	console.log("inside start payment");
	let amount = $("#amt").val();
	console.log(amount);

	if(amount == null ||amount == '')
	{
		Swal.fire({
			title: "Error",
			text: "Amount field should not be null!",
			icon: "error"
		});
		return;
	}

	$.ajax({
		url: '../payment/create_order',
		data: JSON.stringify({ amount: amount , info : 'order_request'}),
		contentType: 'application/json',
		type: 'POST',
		dataType: 'json',
		success: function(response) {
			console.log(response);


			let options = {
				key: "rzp_test_GxHBH51k6keEAC",
				amount: response.amount,
				currency: "INR",
				name: "Smart Contact Manager",
				description: "Help us to improve by donating",
				image:
					"https://sugermint.com/wp-content/uploads/2022/01/Razorpay-Startup-Story.jpg",
				order_id:response.id,


				handler:function (response){
					console.log(response.razorpay_payment_id);
					console.log(response.razorpay_order_id);
					console.log(response.razorpay_signature);

					updatePayment(response.razorpay_payment_id,response.razorpay_order_id,'paid')

					console.log("Payment successful");
					Swal.fire({
						title: "Success",
						text: "Payment Success!",
						icon: "success"
					});
				},

				prefill: {
					"name": "",
					"email": "",
					"contact": ""
				},
				notes: {
					"address": "Razorpay Corporate Office"
				},
				theme: {
					"color": "#3399cc"
				}
			};

			let rzp = new Razorpay(options);

			rzp.on('payment.failed', function (response){
				console.log(response.error.code);
				console.log(response.error.description);
				console.log(response.error.source);
				console.log(response.error.step);
				console.log(response.error.reason);
				console.log(response.error.metadata.order_id);
				console.log(response.error.metadata.payment_id);

				Swal.fire({
					title: "Error",
					text: "Payment Failed!",
					icon: "error"
				});
			});

			rzp.open();
		},
		error: function(error) {
			console.log("Something went wrong");
		}
	});

};

function updatePayment(payment_id,order_id,statusx)
{
	$.ajax({
		url: '../payment/update-order',
		data: JSON.stringify({payment_id : payment_id, order_id : order_id,statusx : statusx}),
		contentType: 'application/json',
		type: 'POST',
		dataType: 'json',
		success:function (error){
			console.log(error);
			Swal.fire({
				title: "Success",
				text: "Payment Done..Thank You for your support!",
				icon: "success"
			});
		}
	})
}