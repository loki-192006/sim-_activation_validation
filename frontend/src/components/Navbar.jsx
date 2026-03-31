import React, { useContext } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { motion } from 'framer-motion'
import { AppContext } from '../App'

const stepPaths = ['/', '/validate', '/verify', '/plans', '/address', '/success']

export default function Navbar() {
  const { STEPS } = useContext(AppContext)
  const location = useLocation()
  const currentIndex = stepPaths.indexOf(location.pathname)

  return (
    <header style={styles.header}>
      <div style={styles.inner}>
        {/* Logo */}
        <div style={styles.logo}>
          <div style={styles.logoIcon}>
            <SimIcon />
          </div>
          <span style={styles.logoText}>SIM<span style={styles.logoAccent}>Portal</span></span>
        </div>

        {/* Step Progress */}
        {currentIndex > 0 && (
          <nav style={styles.steps}>
            {STEPS.slice(1).map((step, i) => {
              const idx = i + 1
              const isDone = currentIndex > idx
              const isActive = currentIndex === idx
              return (
                <React.Fragment key={step.path}>
                  {i > 0 && (
                    <div style={{
                      ...styles.connector,
                      background: isDone ? 'var(--color-primary)' : 'var(--color-border)',
                    }} />
                  )}
                  <motion.div
                    style={styles.stepWrap}
                    initial={{ opacity: 0, y: -8 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ delay: i * 0.06 }}
                  >
                    <div style={{
                      ...styles.stepDot,
                      background: isDone
                        ? 'var(--color-primary)'
                        : isActive
                        ? 'var(--color-primary-dim)'
                        : 'transparent',
                      border: `2px solid ${isDone || isActive ? 'var(--color-primary)' : 'var(--color-border)'}`,
                    }}>
                      {isDone ? '✓' : idx}
                    </div>
                    <span style={{
                      ...styles.stepLabel,
                      color: isDone || isActive ? 'var(--color-primary)' : 'var(--color-text-dim)',
                      fontWeight: isActive ? 600 : 400,
                    }}>
                      {step.label}
                    </span>
                  </motion.div>
                </React.Fragment>
              )
            })}
          </nav>
        )}

        <div style={styles.badge}>
          <div style={styles.dot} />
          Live
        </div>
      </div>
    </header>
  )
}

function SimIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <rect x="5" y="2" width="14" height="20" rx="2" ry="2"/>
      <path d="M15 2v4H9V2"/>
      <rect x="9" y="10" width="6" height="6" rx="1"/>
    </svg>
  )
}

const styles = {
  header: {
    position: 'sticky',
    top: 0,
    zIndex: 100,
    background: 'rgba(6, 13, 26, 0.85)',
    backdropFilter: 'blur(20px)',
    WebkitBackdropFilter: 'blur(20px)',
    borderBottom: '1px solid var(--color-border)',
  },
  inner: {
    maxWidth: 1100,
    margin: '0 auto',
    padding: '0 24px',
    height: 64,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    gap: 24,
  },
  logo: {
    display: 'flex',
    alignItems: 'center',
    gap: 10,
    flexShrink: 0,
  },
  logoIcon: {
    width: 36,
    height: 36,
    borderRadius: 10,
    background: 'linear-gradient(135deg, #3B82F6, #06b6d4)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    color: 'white',
    boxShadow: '0 0 16px rgba(59,130,246,0.4)',
  },
  logoText: {
    fontFamily: 'var(--font-display)',
    fontWeight: 700,
    fontSize: 18,
    color: 'var(--color-text)',
    letterSpacing: '-0.3px',
  },
  logoAccent: {
    color: 'var(--color-primary)',
  },
  steps: {
    display: 'flex',
    alignItems: 'center',
    gap: 6,
    flex: 1,
    justifyContent: 'center',
  },
  stepWrap: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    gap: 3,
  },
  stepDot: {
    width: 28,
    height: 28,
    borderRadius: '50%',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontSize: 11,
    fontWeight: 600,
    color: 'white',
    transition: 'all 0.3s ease',
  },
  stepLabel: {
    fontSize: 10,
    fontFamily: 'var(--font-display)',
    letterSpacing: '0.4px',
    textTransform: 'uppercase',
    transition: 'color 0.3s ease',
  },
  connector: {
    height: 2,
    width: 28,
    borderRadius: 2,
    marginBottom: 18,
    transition: 'background 0.4s ease',
  },
  badge: {
    display: 'flex',
    alignItems: 'center',
    gap: 6,
    fontSize: 12,
    color: 'var(--color-success)',
    fontWeight: 600,
    fontFamily: 'var(--font-display)',
    flexShrink: 0,
  },
  dot: {
    width: 7,
    height: 7,
    borderRadius: '50%',
    background: 'var(--color-success)',
    boxShadow: '0 0 8px var(--color-success)',
    animation: 'pulse 2s infinite',
  },
}
