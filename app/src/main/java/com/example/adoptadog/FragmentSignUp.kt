auth = Firebase.auth
emailEditText = view.findViewById(R.id.emailInput)
passwordEditText = view.findViewById(R.id.passwordInput)
nameEditText = view.findViewById(R.id.nameInput)
signupButton = view.findViewById(R.id.signupButton)

userDao = (requireActivity().application as MyApplication).database.userDao()

(activity as? NavHostActivity)?.startLoading()
Handler(Looper.getMainLooper()).postDelayed({
    (activity as? NavHostActivity)?.stopLoading()
}, 1000)

view.findViewById<TextView>(R.id.loginButtonLink).setOnClickListener {
    val intent = Intent(requireContext(), LoginActivity::class.java)
    startActivity(intent)
    requireActivity().finish()
}

signupButton.setOnClickListener {
    val email = emailEditText.text.toString().trim()
    val password = passwordEditText.text.toString().trim()
    val name = nameEditText.text.toString().trim()

    if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
        (activity as? NavHostActivity)?.startLoading()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                (activity as? NavHostActivity)?.stopLoading()
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    firebaseUser?.let { user ->
                        val newUser = User(
                            uid = user.uid,
                            name = name,
                            email = email
                        )

                        // שמירה במסד נתונים מקומי (Room)
                        lifecycleScope.launch(Dispatchers.IO) {
                            userDao.insert(newUser)

                            // שמירה בפיירסטור
                            val db = Firebase.firestore
                            val userData = hashMapOf(
                                "name" to name,
                                "email" to email
                            )
                            db.collection("users").document(user.uid)
                                .set(userData)

                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "ההרשמה הצליחה!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().navigate(R.id.action_fragmentSignUp_to_homePageFragment)
                            }
                        }
                    }
                } else {
                    // טיפול בשגיאות ספציפיות
                    when (task.exception) {
                        is FirebaseAuthWeakPasswordException -> {
                            Toast.makeText(context, "סיסמה חלשה. סיסמה צריכה להכיל לפחות 6 תווים.", Toast.LENGTH_SHORT).show()
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            Toast.makeText(context, "אימייל לא תקין.", Toast.LENGTH_SHORT).show()
                        }
                        is FirebaseAuthUserCollisionException -> {
                            Toast.makeText(context, "משתמש עם אימייל זה כבר קיים.", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(context, "הרשמה נכשלה. אנא נסה שוב.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    } else {
        Toast.makeText(context, "אנא מלא את כל השדות", Toast.LENGTH_SHORT).show()
    }
}
