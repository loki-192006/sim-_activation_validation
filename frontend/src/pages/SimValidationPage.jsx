import React, { useContext, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { motion, AnimatePresence } from 'framer-motion'
import { AppContext } from '../App'
import { simAPI } from '../services/api'
import PageWrapper from '../components/PageWrapper'
import Button from '../components/Button'
import Input from '../components/Input'
import Alert from '../components/Alert'

export default function SimValidationPage() {
  const { updateFlowData } = useContext(AppContext)
  const navigate = useNavigate()

  const [simNumber, setSimNumber] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [validated, setValidated] = useState(null)

  const validate = async () => {
    if (!simNumber.trim()) { setError('Please enter your SIM number.'); return }
    if (simNumber.trim().length < 15) { setError('SIM number must be at least 15 digits.'); return }
    setError('')
    setLoading(true)
    try {
      const res = await simAPI.validate(simNumber.trim())
      const sim = res.data.data
      setValidated(sim)
      updateFlowData('sim', sim)
    } catch (err) {
      setError(err.message)
      setValidated(null)
    } finally {
      setLoading(false)
    }
  }

  const handleKeyDown = (e) => { if (e.key === 'Enter') validate() }

  return (
    <PageWrapper>
      <div style={styles.page}>
        <div style={styles.card} className="glass gradient-border">
          {/* Header */}
          <div style={styles.header}>
            <div style={styles.iconWrap}>
              <SimIcon />
            </div>
            <h2 style={styles.title}>Validate Your SIM</h2>
            <p style={styles.subtitle}>
              Enter the 19-digit SIM card number printed on the back of your SIM tray.
            </p>
          </div>

          {/* Input area */}
          <div style={styles.body}>
            <Input
              label="SIM Card Number"
              placeholder="e.g. 8901260123456789012"
              value={simNumber}
              onChange={e => {
                setSimNumber(e.target.value.replace(/\D/g, ''))
                setError('')
                setValidated(null)
              }}
              onKeyDown={handleKeyDown}
              error={error && !loading ? error : ''}
              hint="Found on the back of your SIM packaging"
              icon={<SimSmallIcon />}
              maxLength={20}
              required
            />

            {/* Character count */}
            <div style={styles.charCount}>
              <span style={{ color: simNumber.length >= 15 ? 'var(--color-success)' : 'var(--color-text-dim)' }}>
                {simNumber.length}
              </span>
              <span style={{ color: 'var(--color-text-dim)' }}> / 19 digits</span>
            </div>

            {error && <Alert type="error" message={error} onClose={() => setError('')} />}

            {/* Success state */}
            <AnimatePresence>
              {validated && (
                <motion.div
                  initial={{ opacity: 0, height: 0 }}
                  animate={{ opacity: 1, height: 'auto' }}
                  exit={{ opacity: 0, height: 0 }}
                  style={styles.successBox}
                >
                  <div style={styles.successIcon}>✓</div>
                  <div>
                    <p style={styles.successTitle}>SIM is Valid & Available</p>
                    <p style={styles.successSub}>SIM #{validated.simNumber} · Status: {validated.status}</p>
                  </div>
                </motion.div>
              )}
            </AnimatePresence>

            <Button
              fullWidth
              loading={loading}
              onClick={validated ? () => navigate('/verify') : validate}
              size="lg"
              icon={validated ? <ArrowIcon /> : <SearchIcon />}
            >
              {validated ? 'Continue to Verification' : 'Validate SIM'}
            </Button>
          </div>

          {/* Demo hint */}
          <div style={styles.demo}>
            <p style={styles.demoLabel}>Demo SIM numbers</p>
            <div style={styles.demoNums}>
              {['8901260123456789012','8901260123456789013'].map(n => (
                <button
                  key={n}
                  style={styles.demoBtn}
                  onClick={() => { setSimNumber(n); setError(''); setValidated(null) }}
                >
                  {n}
                </button>
              ))}
            </div>
          </div>
        </div>
      </div>
    </PageWrapper>
  )
}

function SimIcon() {
  return (
    <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="1.8">
      <rect x="5" y="2" width="14" height="20" rx="2"/>
      <path d="M15 2v4H9V2"/>
      <rect x="9" y="10" width="6" height="6" rx="1"/>
    </svg>
  )
}
function SimSmallIcon() {
  return (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
      <rect x="5" y="2" width="14" height="20" rx="2"/>
      <path d="M15 2v4H9V2"/>
      <rect x="9" y="10" width="6" height="6" rx="1"/>
    </svg>
  )
}
function ArrowIcon() {
  return <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
}
function SearchIcon() {
  return <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5"><circle cx="11" cy="11" r="8"/><path d="M21 21l-4.35-4.35"/></svg>
}

const styles = {
  page: {
    minHeight: 'calc(100vh - 128px)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    padding: '48px 24px',
  },
  card: {
    width: '100%',
    maxWidth: 520,
  },
  header: {
    padding: '36px 36px 28px',
    borderBottom: '1px solid var(--color-border)',
    textAlign: 'center',
  },
  iconWrap: {
    width: 60, height: 60,
    borderRadius: 16,
    background: 'linear-gradient(135deg, #3B82F6, #06b6d4)',
    display: 'flex', alignItems: 'center', justifyContent: 'center',
    margin: '0 auto 20px',
    boxShadow: '0 0 24px rgba(59,130,246,0.35)',
  },
  title: {
    fontFamily: 'var(--font-display)',
    fontSize: 24, fontWeight: 700,
    color: 'var(--color-text)',
    marginBottom: 10, letterSpacing: '-0.5px',
  },
  subtitle: {
    fontSize: 14, color: 'var(--color-text-muted)',
    lineHeight: 1.6, maxWidth: 340, margin: '0 auto',
  },
  body: {
    padding: '28px 36px',
    display: 'flex', flexDirection: 'column', gap: 16,
  },
  charCount: {
    textAlign: 'right', fontSize: 12, fontFamily: 'var(--font-display)', marginTop: -8,
  },
  successBox: {
    background: 'rgba(16,185,129,0.08)',
    border: '1px solid rgba(16,185,129,0.25)',
    borderRadius: 12, padding: '14px 16px',
    display: 'flex', alignItems: 'center', gap: 14,
    overflow: 'hidden',
  },
  successIcon: {
    width: 36, height: 36, borderRadius: '50%',
    background: 'rgba(16,185,129,0.2)',
    display: 'flex', alignItems: 'center', justifyContent: 'center',
    color: 'var(--color-success)', fontSize: 18, fontWeight: 700, flexShrink: 0,
  },
  successTitle: {
    fontSize: 14, fontWeight: 600,
    color: 'var(--color-success)', marginBottom: 2,
    fontFamily: 'var(--font-display)',
  },
  successSub: {
    fontSize: 12, color: 'rgba(16,185,129,0.7)', fontFamily: 'var(--font-body)',
  },
  demo: {
    padding: '0 36px 28px',
  },
  demoLabel: {
    fontSize: 11, color: 'var(--color-text-dim)',
    textTransform: 'uppercase', letterSpacing: '1px',
    fontFamily: 'var(--font-display)', marginBottom: 10,
  },
  demoNums: { display: 'flex', gap: 8, flexWrap: 'wrap' },
  demoBtn: {
    fontSize: 12, padding: '6px 12px', borderRadius: 8,
    background: 'var(--color-primary-dim)',
    border: '1px solid var(--color-border)',
    color: 'var(--color-primary)', cursor: 'pointer',
    fontFamily: 'var(--font-body)', letterSpacing: '0.5px',
    transition: 'all 0.2s',
  },
}
