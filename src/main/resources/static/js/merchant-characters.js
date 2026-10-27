/* =========================================================
   MERCHANT QR PORTAL
   ORIGINAL ANIME-INSPIRED CHARACTER CONTROLLER
   ========================================================= */

/*document.addEventListener("DOMContentLoaded", function () {

    createMerchantMascot();

    setupCharacterActions();

    setupCursorEffects();

    setupSuccessDetection();

});*/


/* =========================================================
   CREATE MASCOT
   ========================================================= */

/*function createMerchantMascot() {

    if (document.getElementById("merchant-mascot")) {
        return;
    }

    const mascot =
        document.createElement("div");

    mascot.id =
        "merchant-mascot";

    mascot.innerHTML = `

        <div class="mascot-message">
            Welcome to Merchant QR Portal!
        </div>

        <div class="mascot-character">

            <div class="mascot-hair"></div>

            <div class="mascot-head">

                <div class="mascot-eye left"></div>

                <div class="mascot-eye right"></div>

                <div class="mascot-mouth"></div>

            </div>

            <div class="mascot-body"></div>

            <div class="mascot-tie"></div>

            <div class="mascot-arm-left"></div>

            <div class="mascot-arm-right"></div>

            <div
                class="mascot-hand"
                style="
                    left: 2px;
                    top: 88px;
                ">
            </div>

            <div
                class="mascot-hand"
                style="
                    right: 0;
                    top: 88px;
                ">
            </div>

            <div class="mascot-leg-left"></div>

            <div class="mascot-leg-right"></div>

        </div>

        <div class="mascot-sparkle one"></div>

        <div class="mascot-sparkle two"></div>

        <div class="mascot-sparkle three"></div>
    `;

    document.body.appendChild(mascot);


    
     * Wait for page rendering before appearing.
     

    setTimeout(function () {

        mascot.classList.add(
            "mascot-visible"
        );

    }, 300);


    
     * Welcome animation.
     

    setTimeout(function () {

        mascot.classList.add(
            "mascot-wave"
        );

        showMascotMessage(
            "Hi! I'm your Merchant QR assistant 👋"
        );

    }, 900);


    setTimeout(function () {

        mascot.classList.remove(
            "mascot-wave"
        );

        hideMascotMessage();

    }, 3500);
}


 =========================================================
   GET MASCOT
   ========================================================= 

function getMascot() {

    return document.getElementById(
        "merchant-mascot"
    );
}


 =========================================================
   CHANGE CHARACTER STATE
   ========================================================= 

function setMascotState(state) {

    const mascot = getMascot();

    if (!mascot) {
        return;
    }

    mascot.classList.remove(
        "mascot-wave",
        "mascot-search",
        "mascot-success",
        "mascot-error",
        "mascot-working"
    );

    if (state) {

        mascot.classList.add(
            "mascot-" + state
        );
    }
}


 =========================================================
   MESSAGE
   ========================================================= 

function showMascotMessage(message) {

    const mascot =
        getMascot();

    if (!mascot) {
        return;
    }

    const bubble =
        mascot.querySelector(
            ".mascot-message"
        );

    if (!bubble) {
        return;
    }

    bubble.textContent =
        message;

    bubble.classList.add(
        "show"
    );
}


function hideMascotMessage() {

    const mascot =
        getMascot();

    if (!mascot) {
        return;
    }

    const bubble =
        mascot.querySelector(
            ".mascot-message"
        );

    if (!bubble) {
        return;
    }

    bubble.classList.remove(
        "show"
    );
}


 =========================================================
   SEARCH / FILTER
   ========================================================= 

function setupCharacterActions() {

    
     * Registration submit.
     

    document.querySelectorAll(
        'form[method="post"]'
    ).forEach(function (form) {

        form.addEventListener(
            "submit",
            function () {

                setMascotState(
                    "working"
                );

                showMascotMessage(
                    "Creating your Merchant QR... ✨"
                );
            }
        );
    });


    
     * Search form.
     

    document.querySelectorAll(
        'form[action="/merchant-registrations"]'
    ).forEach(function (form) {

        form.addEventListener(
            "submit",
            function () {

                setMascotState(
                    "search"
                );

                showMascotMessage(
                    "Let's find those merchant records! 🔎"
                );

                setTimeout(function () {

                    hideMascotMessage();

                }, 2500);
            }
        );
    });


    
     * Merchant type selection.
     

    const merchantType =
        document.getElementById(
            "merchantType"
        );

    if (merchantType) {

        merchantType.addEventListener(
            "change",
            function () {

                setMascotState(
                    "search"
                );

                showMascotMessage(
                    "Checking this merchant type..."
                );

                setTimeout(function () {

                    hideMascotMessage();

                }, 1600);
            }
        );
    }


    
     * Clear button.
     

    document.querySelectorAll(
        "a"
    ).forEach(function (link) {

        const text =
            link.textContent
                .trim()
                .toLowerCase();

        if (
            text === "clear" ||
            text.includes("clear")
        ) {

            link.addEventListener(
                "click",
                function () {

                    setMascotState(
                        "wave"
                    );

                    showMascotMessage(
                        "Filters cleared! Starting fresh ✨"
                    );
                }
            );
        }
    });


    
     * View Default.
     

    document.querySelectorAll(
        "a"
    ).forEach(function (link) {

        const text =
            link.textContent
                .trim()
                .toLowerCase();

        if (
            text.includes("view default")
        ) {

            link.addEventListener(
                "click",
                function () {

                    setMascotState(
                        "search"
                    );

                    showMascotMessage(
                        "Loading today's merchants..."
                    );
                }
            );
        }
    });


    
     * QR buttons.
     

    document.querySelectorAll(
        "button, a"
    ).forEach(function (element) {

        const text =
            element.textContent
                .trim()
                .toLowerCase();

        if (
            text.includes("view qr")
        ) {

            element.addEventListener(
                "click",
                function () {

                    setMascotState(
                        "success"
                    );

                    showMascotMessage(
                        "Here's the QR code! 📱"
                    );

                    setTimeout(function () {

                        hideMascotMessage();

                    }, 2200);
                }
            );
        }
    });


    
     * Download PDF.
     

    document.querySelectorAll(
        "a, button"
    ).forEach(function (element) {

        const text =
            element.textContent
                .trim()
                .toLowerCase();

        if (
            text.includes("download pdf")
        ) {

            element.addEventListener(
                "click",
                function () {

                    setMascotState(
                        "success"
                    );

                    showMascotMessage(
                        "Preparing your PDF! 📄"
                    );
                }
            );
        }
    });


    
     * Download Excel.
     

    document.querySelectorAll(
        "a, button"
    ).forEach(function (element) {

        const text =
            element.textContent
                .trim()
                .toLowerCase();

        if (
            text.includes("download excel")
        ) {

            element.addEventListener(
                "click",
                function () {

                    setMascotState(
                        "success"
                    );

                    showMascotMessage(
                        "Exporting all merchant data! 📊"
                    );
                }
            );
        }
    });
}


 =========================================================
   SUCCESS DETECTION
   ========================================================= 

function setupSuccessDetection() {

    
     * Your registration page can expose
     * a success message or QR modal.
     

    const successElements =
        document.querySelectorAll(
            ".success-message, .alert-success, .qr-modal"
        );

    if (successElements.length === 0) {
        return;
    }


    const observer =
        new MutationObserver(
            function () {

                setMascotState(
                    "success"
                );

                showMascotMessage(
                    "Merchant QR created successfully! 🎉"
                );

                createConfetti();

                setTimeout(function () {

                    hideMascotMessage();

                }, 3500);
            }
        );


    successElements.forEach(
        function (element) {

            observer.observe(
                element,
                {
                    attributes: true,
                    childList: true,
                    subtree: true
                }
            );
        }
    );
}


 =========================================================
   CONFETTI
   ========================================================= 

function createConfetti() {

    const total =
        22;

    for (
        let i = 0;
        i < total;
        i++
    ) {

        const piece =
            document.createElement(
                "div"
            );

        piece.className =
            "mascot-confetti";

        piece.style.left =
            (
                window.innerWidth * 0.45
                +
                Math.random() *
                window.innerWidth * 0.2
            )
            + "px";

        piece.style.top =
            (
                window.innerHeight * 0.35
            )
            + "px";


        const colors = [
            "#2563eb",
            "#f59e0b",
            "#22c55e",
            "#ec4899",
            "#8b5cf6"
        ];

        piece.style.background =
            colors[
                Math.floor(
                    Math.random() *
                    colors.length
                )
            ];


        piece.style.animationDelay =
            (
                Math.random() * 0.25
            )
            + "s";


        document.body.appendChild(
            piece
        );


        setTimeout(
            function () {

                piece.remove();

            },
            1800
        );
    }
}


 =========================================================
   CURSOR SPARK EFFECT
   ========================================================= 

function setupCursorEffects() {

    let lastX = 0;
    let lastY = 0;

    document.addEventListener(
        "mousemove",
        function (event) {

            const distance =
                Math.sqrt(
                    Math.pow(
                        event.clientX - lastX,
                        2
                    )
                    +
                    Math.pow(
                        event.clientY - lastY,
                        2
                    )
                );


            if (distance < 45) {
                return;
            }


            lastX =
                event.clientX;

            lastY =
                event.clientY;


            
             * Don't create too many
             * DOM elements.
             

            if (
                Math.random() > 0.25
            ) {
                return;
            }


            const spark =
                document.createElement(
                    "div"
                );

            spark.className =
                "mascot-cursor-spark";


            spark.style.left =
                event.clientX + "px";

            spark.style.top =
                event.clientY + "px";


            document.body.appendChild(
                spark
            );


            setTimeout(
                function () {

                    spark.remove();

                },
                600
            );
        }
    );
}


 =========================================================
   OPTIONAL PUBLIC FUNCTIONS
   ========================================================= 


 * These functions can also be called
 * from your existing HTML/JavaScript.
 

window.merchantMascot = {

    success: function () {

        setMascotState(
            "success"
        );

        showMascotMessage(
            "Success! Merchant registration completed! 🎉"
        );

        createConfetti();

        setTimeout(
            hideMascotMessage,
            3000
        );
    },


    error: function () {

        setMascotState(
            "error"
        );

        showMascotMessage(
            "Hmm... something needs your attention."
        );

        setTimeout(
            hideMascotMessage,
            2500
        );
    },


    search: function () {

        setMascotState(
            "search"
        );

        showMascotMessage(
            "Searching merchant records... 🔎"
        );
    },


    working: function () {

        setMascotState(
            "working"
        );

        showMascotMessage(
            "Working on it... ✨"
        );
    },


    wave: function () {

        setMascotState(
            "wave"
        );

        showMascotMessage(
            "Hello! 👋"
        );

        setTimeout(
            hideMascotMessage,
            2200
        );
    },


    hide: function () {

        hideMascotMessage();
    }

};*/