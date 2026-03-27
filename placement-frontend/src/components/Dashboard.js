import { useEffect, useState } from "react"
import axios from "../services/api"
import { useNavigate, Link } from "react-router-dom"
import "../Dashboard.css"

function Dashboard() {

    const [profile, setProfile] = useState(null)
    const [domains, setDomains] = useState([])
    const [goals, setGoals] = useState([])

    const studentId = Number(localStorage.getItem("studentId"))
    const navigate = useNavigate()

    // eslint-disable-next-line
    useEffect(() => {
        fetchData()
    }, [])

    const fetchData = () => {

        if (!studentId) {
            alert("Login expired. Please login again.")
            navigate("/login")
            return
        }

        // PROFILE
        axios.get(`/profile/${studentId}`)
            .then(res => setProfile(res.data))
            .catch(err => console.error("Profile error", err))

        // DOMAINS
        axios.get(`/domain/student/${studentId}`)
            .then(res => {
                setDomains(Array.isArray(res.data) ? res.data : [])
            })
            .catch(err => {
                console.error("Domain error", err)
                setDomains([])
            })

        // GOALS (USER + GLOBAL)
        axios.get(`/goal/withProgress/${studentId}`)
            .then(res => {
                console.log("Goals API:", res.data)
                setGoals(Array.isArray(res.data) ? res.data : [])
            })
            .catch(err => {
                console.error("Goal error", err)
                setGoals([])
            })
    }

    const deleteGoal = async (goalId) => {
        try {
            await axios.delete(`/goal/${goalId}`)
            setGoals(goals.filter(g => g.id !== goalId))
        } catch {
            alert("Error deleting goal")
        }
    }

    const calculateScore = () => {
        if (!goals.length) return 0

        let totalTarget = 0
        let totalCompleted = 0

        goals.forEach(g => {
            // ❗ Ignore global goals (they no longer store completed)
            if (!g.globalGoal) {
                totalTarget += g.target || 0
                totalCompleted += g.completed || 0
            }
        })

        if (totalTarget === 0) return 0

        return Math.round((totalCompleted / totalTarget) * 100)
    }

    const handleLogout = () => {
        localStorage.removeItem("studentId")
        navigate("/login")
    }

    return (

        <div className="dashboard">

            {/* HEADER */}
            <div className="dashboard-header">
                <h1 className="dashboard-title">Placement Preparation Dashboard</h1>
                <button className="logout-btn" onClick={handleLogout}>Logout</button>
            </div>

            {/* PROFILE */}
            <div className="card-container">
                <div className="card"><h3>Skills</h3><p>{profile?.skills}</p></div>
                <div className="card"><h3>Target Company</h3><p>{profile?.targetCompany}</p></div>
                <div className="card"><h3>Preferred Role</h3><p>{profile?.role}</p></div>
                <div className="card"><h3>Preferred Domain</h3><p>{profile?.preferredDomain}</p></div>
                <div className="card score-card"><h3>Readiness Score</h3><p>{calculateScore()}%</p></div>
            </div>

            {/* GOALS */}
            <div className="goal-section">

                <div className="goal-header">
                    <h2>Your Goals</h2>

                    <Link to="/goal">
                        <button className="add-goal-btn">Add Goal</button>
                    </Link>
                </div>

                {goals.length === 0 && <p>No goals added yet</p>}

                {goals.map(goal => {

                    // ✅ FIXED percent logic
                    let percent = goal.globalGoal ? 0 : (
                        goal.target
                            ? Math.round((goal.completed / goal.target) * 100)
                            : 0
                    )

                    return (

                        <div key={goal.id} className="goal-card">

                            <h4>
                                {goal.title}
                                {goal.globalGoal && (
                                    <span style={{
                                        marginLeft: "10px",
                                        color: "#8b5cf6",
                                        fontSize: "12px"
                                    }}>
                                        GLOBAL
                                    </span>
                                )}
                            </h4>

                            <div className="progress-bar">
                                <div
                                    className="progress-fill"
                                    style={{ width: `${percent}%` }}
                                ></div>
                            </div>

                            {/* ✅ FIXED TEXT */}
                            <p>
                                {goal.globalGoal
                                    ? "Test not attempted yet"
                                    : `${goal.completed} / ${goal.target}`}
                            </p>

                            <div className="goal-buttons">

                                <button
                                    className="test-btn"
                                    onClick={() => navigate(`/test/${goal.id}`)}
                                >
                                    Take Test
                                </button>

                                {!goal.globalGoal && (
                                    <button
                                        className="delete-btn"
                                        onClick={() => deleteGoal(goal.id)}
                                    >
                                        Delete
                                    </button>
                                )}

                            </div>

                        </div>
                    )
                })}

            </div>

            {/* DOMAINS */}
            <div className="domain-section">
                <h3>Preparation Domains</h3>
                <ul>
                    {domains.map(d => (
                        <li key={d.id}>{d.name}</li>
                    ))}
                </ul>
            </div>

        </div>
    )
}

export default Dashboard