import React from 'react'
import { motion } from 'framer-motion'

export default function PageWrapper({ children, style }) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 24 }}
      animate={{ opacity: 1, y: 0 }}
      exit={{ opacity: 0, y: -16 }}
      transition={{ duration: 0.4, ease: [0.22, 1, 0.36, 1] }}
      style={{ minHeight: 'calc(100vh - 128px)', ...style }}
    >
      {children}
    </motion.div>
  )
}
