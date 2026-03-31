import React, { useContext, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { motion } from 'framer-motion'
import { AppContext } from '../App'
import PageWrapper from '../components/PageWrapper'
import Button from '../components/Button'

export default function SuccessPage() {
  const { flowData, updateFlowData } = useContext(AppContext)
  const navigate = useNavigate()

  const { sim, customer, offer } = flowData

  useEffect(() => {
    if (!customer) navigate('/')
  }, [])

  if (!customer) return null

  const handleNewActivation = () => {
    updateFlowData('sim', null)
    updateFlowData('customer', null)
    updateFlowData('offer', null)
    updateFlowData('activation', null)
    navigate('/')
  }

  const details = [
    { label: 'SIM Number',   value: sim?.simNumber },
    { label: 'Customer',     value: `${customer?.firstName} ${customer?.lastName}` },
    { label: 'Email',        value: customer?.email },
    { label: 'Plan',         value: offer?.planName },
    { label: 'Validity',     value: offer?.validity },
    { label: 'Amount Paid',  value: `₹${offer?.price}` },
    { label: 'Status',       value: 'ACTIVE', highlight: true },
  ]

  return (
    <PageWrapper>
      <div style={styles.page}>
        {/* Confetti-like particles */}
        <div style={styles.particles}>
          {[...Array(12)].map((_, i) => (
            <motion.div
              key={i}
              style={{
                ...styles.particle,
                left: `${8 + i * 8}%`,
                background: ['#3B82F6','#06b6d4','#8b5cf6','#10b981','#f59e0b'][i % 5],
              }}
              initial={{ y: 0, opacity: 1, scale: 1 }}
              animate={{ y: -120 - Math.random() * 80, opacity: 0, scale: 0, rotate: 360 }}
              transition={{ delay: 0.3 + i * 0.08, duration: 1.5, ease: 'easeOut' }}
            />
          ))}
        </div>

        <div style={styles.card} className="glass gradient-border">
          {/* Animated checkmark */}
          <div style={styles.checkArea}>
            <motion.div
              style={styles.checkOuter}
              initial={{ scale: 0, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              transition={{ duration: 0.5, ease: [0.22, 1, 0.36, 1] }}
            >
              <motion.div
                style={styles.checkInner}
                initial={{ scale: 0 }}
                animate={{ scale: 1 }}
                transition={{ delay: 0.3, duration: 0.4, ease: [0.22, 1, 0.36, 1] }}
              >
                <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                  <motion.polyline
                    points="20 6 9 17 4 12"
                    initial={{ pathLength: 0 }}
                    animate={{ pathLength: 1 }}
                    transition={{ delay: 0.5, duration: 0.5, ease: 'easeOut' }}
                  />
                </svg>
              </motion.div>
            </motion.div>

            <motion.div
              initial={{ opacity: 0, y: 12 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.6, duration: 0.5 }}
            >
              <h2 style={styles.title}>SIM Activated Successfully!</h2>
              <p style={styles.subtitle}>
                Your SIM is now live. Welcome to the network!
              </p>
            </motion.div>
          </div>

          {/* Activation ID */}
          <motion.div
            style={styles.activationBadge}
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ delay: 0.7 }}
          >
            <span style={styles.activationLabel}>Activation Reference</span>
            <span style={styles.activationId}>
              ACT-{Date.now().toString(36).toUpperCase()}
            </span>
          </motion.div>

          {/* Summary details */}
          <motion.div
            style={styles.details}
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.8 }}
          >
            <h3 style={styles.detailsTitle}>Activation Summary</h3>
            <div style={styles.detailsGrid}>
              {details.map((d, i) => (
                <motion.div
                  key={d.label}
                  style={styles.detailRow}
                  initial={{ opacity: 0, x: -10 }}
                  animate={{ opacity: 1, x: 0 }}
                  transition={{ delay: 0.85 + i * 0.05 }}
                >
                  <span style={styles.detailLabel}>{d.label}</span>
                  <span style={{
                    ...styles.detailValue,
                    color: d.highlight ? 'var(--color-success)' : 'var(--color-text)',
                    background: d.highlight ? 'rgba(16,185,129,0.1)' : 'transparent',
                    padding: d.highlight ? '2px 10px' : '0',
                    borderRadius: d.highlight ? 100 : 0,
                    fontWeight: d.highlight ? 700 : 600,
                  }}>
                    {d.value}
                  </span>
                </motion.div>
              ))}
            </div>
          </motion.div>

          {/* Actions */}
          <motion.div
            style={styles.actions}
            initial={{ opacity: 0, y: 12 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 1.1 }}
          >
            <Button variant="ghost" onClick={() => window.print()}>
              🖨 Print Summary
            </Button>
            <Button onClick={handleNewActivation} icon={<PlusIcon />}>
              Activate Another SIM
            </Button>
          </motion.div>

          {/* Bottom note */}
          <motion.p
            style={styles.note}
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 1.2 }}
          >
            📱 Your SIM will be active within 30 minutes. If you face any issues, call <strong style={{ color: 'var(--color-primary)' }}>1800-XXX-XXXX</strong>
          </motion.p>
        </div>
      </div>
    </PageWrapper>
  )
}

function PlusIcon() {
  return <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
}

const styles = {
  page: {
    minHeight: 'calc(100vh - 128px)',
    display: 'flex', alignItems: 'center', justifyContent: 'center',
    padding: '48px 24px',
    position: 'relative',
  },
  particles: {
    position: 'absolute', top: '30%', left: 0, right: 0,
    pointerEvents: 'none', zIndex: 0,
  },
  particle: {
    position: 'absolute',
    width: 10, height: 10, borderRadius: 3,
  },
  card: {
    width: '100%', maxWidth: 600,
    position: 'relative', zIndex: 1,
    padding: '48px 40px',
    textAlign: 'center',
  },
  checkArea: {
    display: 'flex', flexDirection: 'column', alignItems: 'center',
    gap: 24, marginBottom: 32,
  },
  checkOuter: {
    width: 96, height: 96, borderRadius: '50%',
    background: 'rgba(16,185,129,0.15)',
    border: '2px solid rgba(16,185,129,0.4)',
    display: 'flex', alignItems: 'center', justifyContent: 'center',
    boxShadow: '0 0 40px rgba(16,185,129,0.3)',
  },
  checkInner: {
    width: 68, height: 68, borderRadius: '50%',
    background: 'linear-gradient(135deg, #10b981, #059669)',
    display: 'flex', alignItems: 'center', justifyContent: 'center',
    boxShadow: '0 4px 20px rgba(16,185,129,0.5)',
  },
  title: {
    fontFamily: 'var(--font-display)', fontSize: 28, fontWeight: 800,
    color: 'var(--color-text)', letterSpacing: '-1px', marginBottom: 8,
  },
  subtitle: {
    fontSize: 15, color: 'var(--color-text-muted)', lineHeight: 1.6,
  },
  activationBadge: {
    display: 'inline-flex', flexDirection: 'column', alignItems: 'center', gap: 4,
    background: 'var(--color-primary-dim)',
    border: '1px solid var(--color-border-bright)',
    borderRadius: 12, padding: '12px 24px', marginBottom: 32,
  },
  activationLabel: {
    fontSize: 11, color: 'var(--color-text-dim)',
    textTransform: 'uppercase', letterSpacing: '1px',
    fontFamily: 'var(--font-display)',
  },
  activationId: {
    fontSize: 16, fontWeight: 700, color: 'var(--color-primary)',
    fontFamily: 'var(--font-display)', letterSpacing: '1px',
  },
  details: {
    background: 'rgba(255,255,255,0.02)',
    border: '1px solid var(--color-border)',
    borderRadius: 16, padding: '24px', marginBottom: 28, textAlign: 'left',
  },
  detailsTitle: {
    fontFamily: 'var(--font-display)', fontSize: 13, fontWeight: 700,
    color: 'var(--color-text-dim)', textTransform: 'uppercase',
    letterSpacing: '1px', marginBottom: 16,
  },
  detailsGrid: { display: 'flex', flexDirection: 'column', gap: 12 },
  detailRow: {
    display: 'flex', justifyContent: 'space-between', alignItems: 'center',
  },
  detailLabel: {
    fontSize: 13, color: 'var(--color-text-muted)', fontFamily: 'var(--font-body)',
  },
  detailValue: {
    fontSize: 13, fontFamily: 'var(--font-display)',
  },
  actions: {
    display: 'flex', gap: 14, justifyContent: 'center',
    flexWrap: 'wrap', marginBottom: 24,
  },
  note: {
    fontSize: 13, color: 'var(--color-text-dim)',
    lineHeight: 1.6, fontFamily: 'var(--font-body)',
  },
}
