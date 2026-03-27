import { useEffect, useState } from "react"
import axios from "../services/api"
import { useParams, useNavigate } from "react-router-dom"
import "../Test.css"

function Test() {

    const { goalId } = useParams()

    const [questions, setQuestions] = useState([])
    const [answers, setAnswers] = useState({})
    const [showPopup, setShowPopup] = useState(false)

    const navigate = useNavigate()

    // ✅ SAFE JSON EXTRACTOR
    const extractJSON = (text, startChar, endChar) => {
        try {
            let start = text.indexOf(startChar)
            let end = text.lastIndexOf(endChar) + 1
            return JSON.parse(text.substring(start, end))
        } catch {
            return null
        }
    }

    useEffect(() => {

        axios.get(`/test/${goalId}`)
            .then(res => {

                try {
                    let aiText = res.data.choices[0].message.content
                    let parsed = extractJSON(aiText, "[", "]")
                    setQuestions(Array.isArray(parsed) ? parsed : [])
                } catch (e) {
                    console.log("Parsing error", e)
                    setQuestions([])
                }

            })
            .catch(err => {
                console.error("API Error:", err)
                setQuestions([])
            })

    }, [goalId])


    const handleAnswer = (qIndex, value) => {
        setAnswers({
            ...answers,
            [qIndex]: value
        })
    }


    const confirmSubmit = async () => {

        let score = 0

        for (let i = 0; i < questions.length; i++) {

            let q = questions[i]

            if (q.options) {

                let selected = answers[i]

                if (selected === q.correctAnswer) {
                    score++
                }

                else if (q.correctAnswer && q.correctAnswer.length === 1) {
                    const selectedIndex = q.options.findIndex(opt => opt === selected)
                    const correctIndex = q.correctAnswer.charCodeAt(0) - 65

                    if (selectedIndex === correctIndex) {
                        score++
                    }
                }
            }

            else {
                try {
                    const res = await axios.post("/test/evaluate", {
                        question: q.question,
                        code: answers[i] || ""
                    })

                    let aiText = res.data.choices[0].message.content
                    let parsed = extractJSON(aiText, "{", "}")

                    if (parsed?.score === 1) {
                        score++
                    }

                } catch (err) {
                    console.error("Evaluation error", err)
                }
            }
        }

        const studentId = localStorage.getItem("studentId")

        await axios.put(`/goal/updateScore/${goalId}/${score}/${studentId}`)

        alert(`Your Score: ${score}/${questions.length}`)

        navigate("/dashboard")
    }


    return (

        <div className="testPage">

            <div className="testCard">

                <h1 className="testTitle">AI Generated Test</h1>

                {questions.map((q, i) => (

                    <div key={i} className="questionBlock">

                        <h3 className="question">
                            {i + 1}. {q.question}
                        </h3>

                        {/* ✅ MCQ UI */}
                        {q.options ? (

                            <div className="options">

                                {q.options.map((opt, index) => (

                                    <label key={index} className="optionItem">

                                        <div className="radioArea">
                                            <input
                                                type="radio"
                                                name={`q${i}`}
                                                value={opt}
                                                checked={answers[i] === opt}
                                                onChange={() => handleAnswer(i, opt)}
                                            />
                                        </div>

                                        <div className="optionText">
                                            {opt}
                                        </div>

                                    </label>

                                ))}

                            </div>

                        ) : (

                            // ✅ CODING
                            <div>

                                <p><b>Input:</b> {q.input}</p>
                                <p><b>Output:</b> {q.output}</p>
                                <p><b>Explanation:</b> {q.explanation}</p>

                                <textarea
                                    placeholder="Write your code here..."
                                    value={answers[i] || ""}
                                    onChange={(e) => handleAnswer(i, e.target.value)}
                                />

                            </div>

                        )}

                    </div>

                ))}

                {questions.length > 0 && (
                    <button className="submitBtn" onClick={() => setShowPopup(true)}>
                        Submit Test
                    </button>
                )}

            </div>

            {/* ✅ POPUP */}
            {showPopup && (
                <div className="popupOverlay">

                    <div className="popupBox">

                        <h3>Submit Test?</h3>
                        <p>Are you sure you want to submit?</p>

                        <div className="popupButtons">

                            <button
                                className="cancelBtn"
                                onClick={() => setShowPopup(false)}
                            >
                                Cancel
                            </button>

                            <button
                                className="confirmBtn"
                                onClick={confirmSubmit}
                            >
                                Submit
                            </button>

                        </div>

                    </div>

                </div>
            )}

        </div>
    )
}

export default Test