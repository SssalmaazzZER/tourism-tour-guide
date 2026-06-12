"use client"

import { motion } from "framer-motion"
import { MessageCircle, Phone, Mail, Globe, Send } from "lucide-react"

export function QuickActions() {
  const actions = [
    { icon: MessageCircle, label: "Chat", gradient: "from-accent to-majorelle" },
    { icon: Phone, label: "Call", gradient: "from-emerald to-accent" },
    { icon: Mail, label: "Email", gradient: "from-primary to-secondary" },
    { icon: Globe, label: "Web", gradient: "from-secondary to-saffron" },
  ]

  return (
    <section className="px-4 py-6">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="rounded-3xl bg-gradient-to-br from-primary/10 via-accent/5 to-secondary/10 border border-border/50 p-5"
      >
        <div className="flex items-center justify-between mb-4">
          <div>
            <h2 className="text-lg font-semibold">Need Help?</h2>
            <p className="text-xs text-muted-foreground">Connect with local guides & services</p>
          </div>
          <motion.div
            className="w-12 h-12 rounded-xl bg-gradient-to-br from-primary to-accent flex items-center justify-center"
            animate={{
              boxShadow: [
                "0 0 20px oklch(0.65 0.18 45 / 0.3)",
                "0 0 30px oklch(0.65 0.18 45 / 0.5)",
                "0 0 20px oklch(0.65 0.18 45 / 0.3)",
              ],
            }}
            transition={{ duration: 2, repeat: Infinity }}
          >
            <Send className="w-5 h-5 text-background" />
          </motion.div>
        </div>

        <div className="grid grid-cols-4 gap-3">
          {actions.map((action, index) => (
            <motion.button
              key={action.label}
              initial={{ opacity: 0, scale: 0.8 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ delay: 0.1 * index + 0.3 }}
              className={`flex flex-col items-center gap-2 p-3 rounded-xl bg-gradient-to-br ${action.gradient}`}
              whileHover={{ scale: 1.08, y: -3 }}
              whileTap={{ scale: 0.95 }}
            >
              <action.icon className="w-5 h-5 text-background" />
              <span className="text-[10px] font-medium text-background">{action.label}</span>
            </motion.button>
          ))}
        </div>
      </motion.div>
    </section>
  )
}
