import React, { useContext, useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { motion } from 'framer-motion'
import { AppContext } from '../App'
import { offerAPI } from '../services/api'
import PageWrapper from '../components/PageWrapper'
import Button from '../components/Button'
import Alert from '../components/Alert'

export default function PlanSelectionPage() {
  const { flowData, updateFlowData } = useContext(AppContext)
  const navigate = useNavigate()

  const [offers, setOffers] = useState([])
  const [selected, setSelected] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  if (!flowData.customer) { navigate('/verify'); return null }

  useEffect(() => {
    offerAPI.getAll()
      .then(res => setOffers(res.data.data || []))
      .catch(err => setError(err.message))
      .finally(() => setLoading(false))
  }, [])

  const handleContinue = () => {
    if (!selected) { setError('Please select a plan to continue.'); return }
    updateFlowData('offer', selected)
    navigate('/address')
  }

  const colors = [
    { accent: '#3B82F6', bg: 'rgba(59,130,246,0.06)', glow: 'rgba(59,130,246,0.3)' },
    { accent: '#06b6d4', bg: 'rgba(6,182,212,0.06)',  glow: 'rgba(6,182,212,0.3)' },
    { accent: '#8b5cf6', bg: 'rgba(139,92,246,0.06)', glow: 'rgba(139,92,246,0.3)' },
  ]

  return (
    <PageWrapper>
      <div style={styles.page}>
        <motion.div
          style={styles.header}
          initial={{ opacity: 0, y: -16 }}
          animate={{ opacity: 1, y: 0 }}
        >
          <div style={styles.iconWrap}><ZapIcon /></div>
          <h2 style={styles.title}>Choose Your Plan</h2>
          <p style={styles.subtitle}>Select the plan that best fits your needs. You can always upgrade later.</p>
        </motion.div>

        {error && (
          <div style={{ maxWidth: 900, margin: '0 auto 20px', padding: '0 24px' }}>
            <Alert type="error" message={error} onClose={() => setError('')} />
          </div>
        )}

        {loading ? (
          <div style={styles.skeletonGrid}>
            {[1,2,3].map(i => <div key={i} style={styles.skeleton} className="glass" />)}
          </div>
        ) : (
          <div style={styles.grid}>
            {offers.map((offer, i) => {
              const col = colors[i % colors.length]
              const isSelected = selected?.offerId === offer.offerId
              const isPopular = offer.popular

              return (
                <motion.div
                  key={offer.offerId}
                  initial={{ opacity: 0, y: 30 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ delay: i * 0.1, duration: 0.5, ease: [0.22, 1, 0.36, 1] }}
                  whileHover={{ y: -6 }}
                  onClick={() => { setSelected(offer); setError('') }}
                  style={{
                    ...styles.card,
                    borderColor: isSelected ? col.accent : 'var(--color-border)',
                    background: isSelected ? col.bg : 'rgba(12,24,41,0.7)',
                    boxShadow: isSelected ? `0 0 32px ${col.glow}, 0 8px 32px rgba(0,0,0,0.4)` : 'var(--shadow-card)',
                    cursor: 'pointer',
                    transform: isSelected ? 'scale(1.02)' : 'scale(1)',
                  }}
                >
                  {isPopular && (
                    <div style={{ ...styles.popularBadge, background: col.accent }}>
                      ★ Most Popular
                    </div>
                  )}

                  {/* Plan header */}
                  <div style={{ ...styles.planAccent, background: col.accent }} />

                  <div style={styles.planTop}>
                    <h3 style={{ ...styles.planName, color: col.accent }}>{offer.planName}</h3>
                    <div style={styles.priceRow}>
                      <span style={styles.currency}>₹</span>
                      <span style={styles.price}>{Math.floor(offer.price)}</span>
                      <span style={styles.priceSub}>/{offer.validity}</span>
                    </div>
                    <p style={styles.planDesc}>{offer.description}</p>
                  </div>

                  <div style={styles.divider} />

                  {/* Features */}
                  <div style={styles.features}>
                    {[
                      { icon: '📶', label: 'Data',  value: offer.dataLimit },
                      { icon: '📞', label: 'Calls', value: offer.calls },
                      { icon: '💬', label: 'SMS',   value: offer.sms },
                    ].map(f => (
                      <div key={f.label} style={styles.feature}>
                        <span style={styles.featureIcon}>{f.icon}</span>
                        <span style={styles.featureLabel}>{f.label}</span>
                        <span style={{ ...styles.featureVal, color: col.accent }}>{f.value}</span>
                      </div>
                    ))}
                  </div>

                  {/* Select indicator */}
                  <div style={{
                    ...styles.selectIndicator,
                    borderColor: isSelected ? col.accent : 'var(--color-border)',
                    background: isSelected ? col.accent : 'transparent',
                  }}>
                    {isSelected && <span style={{ color: 'white', fontSize: 14, fontWeight: 700 }}>✓</span>}
                  </div>
                </motion.div>
              )
            })}
          </div>
        )}

        {/* Footer actions */}
        {selected && (
          <motion.div
            style={styles.footer}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
          >
            <div style={styles.selectedSummary}>
              <span style={styles.selectedLabel}>Selected:</span>
              <span style={styles.selectedPlan}>{selected.planName}</span>
              <span style={styles.selectedPrice}>₹{selected.price} / {selected.validity}</span>
            </div>
            <div style={styles.footerActions}>
              <Button variant="ghost" onClick={() => navigate('/verify')}>← Back</Button>
              <Button onClick={handleContinue} icon={<ArrowIcon />}>Continue to Address</Button>
            </div>
          </motion.div>
        )}

        {!selected && !loading && (
          <div style={{ textAlign: 'center', marginTop: 24 }}>
            <Button variant="ghost" onClick={() => navigate('/verify')}>← Back</Button>
          </div>
        )}
      </div>
    </PageWrapper>
  )
}

function ZapIcon() {
  return <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="1.8"><polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/></svg>
}
function ArrowIcon() {
  return <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
}

const styles = {
  page: {
    padding: '48px 24px 80px',
    maxWidth: 1100, margin: '0 auto',
  },
  header: {
    textAlign: 'center', marginBottom: 48,
  },
  iconWrap: {
    width: 60, height: 60, borderRadius: 16,
    background: 'linear-gradient(135deg, #3B82F6, #06b6d4)',
    display: 'flex', alignItems: 'center', justifyContent: 'center',
    margin: '0 auto 20px',
    boxShadow: '0 0 24px rgba(59,130,246,0.35)',
  },
  title: {
    fontFamily: 'var(--font-display)', fontSize: 30, fontWeight: 800,
    color: 'var(--color-text)', letterSpacing: '-1px', marginBottom: 10,
  },
  subtitle: {
    fontSize: 15, color: 'var(--color-text-muted)', maxWidth: 480, margin: '0 auto',
  },
  grid: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
    gap: 24,
    marginBottom: 40,
  },
  skeletonGrid: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
    gap: 24, marginBottom: 40,
  },
  skeleton: {
    height: 400, borderRadius: 20,
    animation: 'pulse 1.5s ease-in-out infinite',
  },
  card: {
    borderRadius: 20,
    border: '1px solid',
    padding: 28,
    position: 'relative',
    overflow: 'hidden',
    transition: 'all 0.3s ease',
    backdropFilter: 'blur(16px)',
  },
  planAccent: {
    position: 'absolute', top: 0, left: 0, right: 0, height: 3,
  },
  popularBadge: {
    position: 'absolute', top: 16, right: 16,
    fontSize: 11, fontWeight: 700,
    color: 'white', padding: '4px 10px', borderRadius: 100,
    fontFamily: 'var(--font-display)', letterSpacing: '0.5px',
  },
  planTop: { marginBottom: 24 },
  planName: {
    fontFamily: 'var(--font-display)', fontSize: 14, fontWeight: 700,
    letterSpacing: '2px', textTransform: 'uppercase', marginBottom: 12,
  },
  priceRow: {
    display: 'flex', alignItems: 'baseline', gap: 4, marginBottom: 10,
  },
  currency: {
    fontSize: 22, fontWeight: 600, color: 'var(--color-text)', fontFamily: 'var(--font-display)',
  },
  price: {
    fontSize: 52, fontWeight: 800, color: 'var(--color-text)',
    fontFamily: 'var(--font-display)', letterSpacing: '-2px', lineHeight: 1,
  },
  priceSub: {
    fontSize: 14, color: 'var(--color-text-muted)', fontFamily: 'var(--font-body)',
  },
  planDesc: {
    fontSize: 13, color: 'var(--color-text-muted)', lineHeight: 1.5,
  },
  divider: { height: 1, background: 'var(--color-border)', marginBottom: 20 },
  features: { display: 'flex', flexDirection: 'column', gap: 12, marginBottom: 24 },
  feature: {
    display: 'flex', alignItems: 'center', gap: 10,
  },
  featureIcon: { fontSize: 16, flexShrink: 0, width: 24 },
  featureLabel: {
    fontSize: 13, color: 'var(--color-text-muted)', flex: 1,
    fontFamily: 'var(--font-body)',
  },
  featureVal: {
    fontSize: 13, fontWeight: 600,
    fontFamily: 'var(--font-display)',
  },
  selectIndicator: {
    width: 28, height: 28, borderRadius: '50%',
    border: '2px solid', margin: '0 auto',
    display: 'flex', alignItems: 'center', justifyContent: 'center',
    transition: 'all 0.25s ease',
  },
  footer: {
    background: 'rgba(12,24,41,0.9)',
    border: '1px solid var(--color-border)',
    borderRadius: 16, padding: '20px 28px',
    display: 'flex', alignItems: 'center',
    justifyContent: 'space-between', gap: 20, flexWrap: 'wrap',
    backdropFilter: 'blur(20px)',
    position: 'sticky', bottom: 24,
  },
  selectedSummary: { display: 'flex', alignItems: 'center', gap: 12, flexWrap: 'wrap' },
  selectedLabel: { fontSize: 13, color: 'var(--color-text-dim)', fontFamily: 'var(--font-body)' },
  selectedPlan: {
    fontSize: 15, fontWeight: 700, color: 'var(--color-text)',
    fontFamily: 'var(--font-display)',
  },
  selectedPrice: {
    fontSize: 13, color: 'var(--color-primary)', fontFamily: 'var(--font-body)',
    background: 'var(--color-primary-dim)', padding: '3px 10px', borderRadius: 100,
  },
  footerActions: { display: 'flex', gap: 12, alignItems: 'center' },
}
