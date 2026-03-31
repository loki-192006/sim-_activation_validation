import React from 'react'
import { motion, AnimatePresence } from 'framer-motion'

export default function Alert({ type = 'error', message, onClose }) {
  if (!message) return null

  const config = {
    error:   { bg: 'rgba(239,68,68,0.1)',   border: 'rgba(239,68,68,0.3)',   color: '#fca5a5', icon: '✕' },
    success: { bg: 'rgba(16,185,129,0.1)',  border: 'rgba(16,185,129,0.3)',  color: '#6ee7b7', icon: '✓' },
    info:    { bg: 'rgba(59,130,246,0.1)',   border: 'rgba(59,130,246,0.3)',  color: '#93c5fd', icon: 'ℹ' },
    warning: { bg: 'rgba(245,158,11,0.1)',  border: 'rgba(245,158,11,0.3)',  color: '#fcd34d', icon: '!' },
  }

  const { bg, border, color, icon } = config[type]

  return (
    <AnimatePresence>
      <motion.div
        initial={{ opacity: 0, y: -8, scale: 0.98 }}
        animate={{ opacity: 1, y: 0, scale: 1 }}
        exit={{ opacity: 0, scale: 0.97 }}
        style={{
          background: bg,
          border: `1px solid ${border}`,
          borderRadius: 12,
          padding: '14px 16px',
          display: 'flex',
          alignItems: 'flex-start',
          gap: 12,
        }}
      >
        <span style={{
          width: 22, height: 22,
          borderRadius: '50%',
          background: border,
          display: 'flex', alignItems: 'center', justifyContent: 'center',
          color, fontSize: 12, fontWeight: 700,
          flexShrink: 0, marginTop: 1,
        }}>
          {icon}
        </span>
        <p style={{ fontSize: 14, color, fontFamily: 'var(--font-body)', flex: 1, lineHeight: 1.5 }}>
          {message}
        </p>
        {onClose && (
          <button
            onClick={onClose}
            style={{ background: 'none', border: 'none', color, cursor: 'pointer', fontSize: 16, opacity: 0.7, padding: 0 }}
          >×</button>
        )}
      </motion.div>
    </AnimatePresence>
  )
}
