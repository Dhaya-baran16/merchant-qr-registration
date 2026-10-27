document.addEventListener("DOMContentLoaded", function () {

    /* =========================================================
       BUTTON RIPPLE EFFECT
       ========================================================= */

    document.querySelectorAll(".btn").forEach(function (button) {

        button.addEventListener("click", function (event) {

            const ripple = document.createElement("span");

            const rect = button.getBoundingClientRect();

            const size = Math.max(
                rect.width,
                rect.height
            );

            const x =
                event.clientX - rect.left - size / 2;

            const y =
                event.clientY - rect.top - size / 2;

            ripple.classList.add("button-ripple");

            ripple.style.width = size + "px";
            ripple.style.height = size + "px";
            ripple.style.left = x + "px";
            ripple.style.top = y + "px";

            button.appendChild(ripple);

            setTimeout(function () {
                ripple.remove();
            }, 600);
        });
    });


	/* =========================================================
	   SUBMIT BUTTON - LOADING ANIMATION
	   ========================================================= */

	document.querySelectorAll("form").forEach(function (form) {

	    form.addEventListener("submit", function (event) {

	        const submitButton =
	            form.querySelector(
	                'button[type="submit"]'
	            );

	        if (!submitButton) {
	            return;
	        }

	        const method =
	            (form.getAttribute("method") || "get")
	                .toLowerCase();

	        if (method === "get") {
	            return;
	        }

	        if (submitButton.dataset.loading === "true") {
	            event.preventDefault();
	            return;
	        }

	        submitButton.dataset.loading = "true";

	        submitButton.classList.add("is-loading");

	        submitButton.dataset.originalText =
	            submitButton.innerHTML;

	        submitButton.innerHTML =
	            '<span class="loading-spinner"></span>' +
	            '<span>Processing...</span>';

	        submitButton.disabled = true;

	    });

	});
    /* =========================================================
       FILTER SEARCH BUTTON
       ========================================================= */

    const filterForms =
        document.querySelectorAll(
            'form[action="/merchant-registrations"]'
        );

    filterForms.forEach(function (form) {

        form.addEventListener("submit", function () {

            const searchButton =
                form.querySelector(
                    'button[type="submit"]'
                );

            if (!searchButton) {
                return;
            }

            searchButton.classList.add(
                "filter-loading"
            );

            searchButton.innerHTML =
                '<span class="mini-spinner"></span>' +
                'Searching...';

            searchButton.disabled = true;
        });
    });


    /* =========================================================
       CLEAR BUTTON ANIMATION
       ========================================================= */

    document.querySelectorAll(
        'a[href="/merchant-registrations"]'
    ).forEach(function (button) {

        const text =
            button.textContent
                .trim()
                .toLowerCase();

        if (
            !text.includes("clear") &&
            !text.includes("view default")
        ) {
            return;
        }

        button.addEventListener("click", function () {

            button.classList.add("clear-clicked");

            const originalHTML =
                button.innerHTML;

            button.innerHTML =
                '<span class="clear-spinner"></span>' +
                'Loading...';

            setTimeout(function () {

                button.innerHTML =
                    originalHTML;

            }, 700);
        });
    });


    /* =========================================================
       INPUT FOCUS ANIMATION
       ========================================================= */

    document.querySelectorAll(
        ".form-control"
    ).forEach(function (input) {

        input.addEventListener("focus", function () {

            this.classList.add(
                "input-active"
            );
        });

        input.addEventListener("blur", function () {

            this.classList.remove(
                "input-active"
            );
        });
    });


    /* =========================================================
       SELECT / DROPDOWN ANIMATION
       ========================================================= */

    document.querySelectorAll(
        "select.form-control"
    ).forEach(function (select) {

        select.addEventListener("change", function () {

            this.classList.add(
                "selection-changed"
            );

            setTimeout(function () {

                select.classList.remove(
                    "selection-changed"
                );

            }, 500);
        });
    });


    /* =========================================================
       TABLE ROW HOVER EFFECT
       ========================================================= */

    document.querySelectorAll(
        "tbody tr"
    ).forEach(function (row) {

        row.addEventListener("mouseenter", function () {

            this.classList.add(
                "row-hover-active"
            );
        });

        row.addEventListener("mouseleave", function () {

            this.classList.remove(
                "row-hover-active"
            );
        });
    });


    /* =========================================================
       CARD MOUSE MOVEMENT EFFECT
       ========================================================= */

    document.querySelectorAll(
        ".card, .summary-card, .export-card"
    ).forEach(function (card) {

        card.addEventListener(
            "mousemove",
            function (event) {

                const rect =
                    card.getBoundingClientRect();

                const x =
                    event.clientX - rect.left;

                const y =
                    event.clientY - rect.top;

                const centerX =
                    rect.width / 2;

                const centerY =
                    rect.height / 2;

                const rotateX =
                    ((y - centerY) / centerY) * -1.2;

                const rotateY =
                    ((x - centerX) / centerX) * 1.2;

                card.style.transform =
                    "perspective(700px) " +
                    "rotateX(" + rotateX + "deg) " +
                    "rotateY(" + rotateY + "deg) " +
                    "translateY(-2px)";
            }
        );

        card.addEventListener(
            "mouseleave",
            function () {

                card.style.transform =
                    "";
            }
        );
    });


    /* =========================================================
       BUTTON HOVER CURSOR GLOW
       ========================================================= */

    document.querySelectorAll(
        ".btn"
    ).forEach(function (button) {

        button.addEventListener(
            "mousemove",
            function (event) {

                const rect =
                    button.getBoundingClientRect();

                const x =
                    event.clientX - rect.left;

                const y =
                    event.clientY - rect.top;

                button.style.setProperty(
                    "--mouse-x",
                    x + "px"
                );

                button.style.setProperty(
                    "--mouse-y",
                    y + "px"
                );
            }
        );
    });


    /* =========================================================
       SMOOTH PAGE LOAD
       ========================================================= */

    document.body.classList.add(
        "page-loaded"
    );


    /* =========================================================
       TABLE ROW ENTRY ANIMATION
       ========================================================= */

    document.querySelectorAll(
        "tbody tr"
    ).forEach(function (row, index) {

        row.style.animationDelay =
            (index * 45) + "ms";

        row.classList.add(
            "table-row-enter"
        );
    });


    /* =========================================================
       MODAL OPEN ANIMATION
       ========================================================= */

    const modal =
        document.querySelector(
            ".qr-modal"
        );

    if (modal) {

        const observer =
            new MutationObserver(
                function () {

                    if (
                        modal.classList.contains(
                            "show"
                        )
                    ) {

                        modal.classList.add(
                            "modal-animated"
                        );

                    } else {

                        modal.classList.remove(
                            "modal-animated"
                        );
                    }
                }
            );

        observer.observe(
            modal,
            {
                attributes: true,
                attributeFilter: [
                    "class",
                    "style"
                ]
            }
        );
    }

});